package spharos.nu.goods.domain.goods.repository.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import spharos.nu.goods.domain.goods.dto.GoodsInfoDto;
import spharos.nu.goods.domain.goods.dto.GoodsSummaryDto;
import spharos.nu.goods.domain.goods.dto.QGoodsInfoDto;
import spharos.nu.goods.domain.goods.dto.QGoodsSummaryDto;
import spharos.nu.goods.domain.goods.entity.Goods;
import spharos.nu.goods.domain.goods.entity.QBiddingCount;
import spharos.nu.goods.domain.goods.entity.QGoods;
import spharos.nu.goods.domain.goods.entity.QImage;
import spharos.nu.goods.domain.goods.entity.QViewsCount;
import spharos.nu.goods.domain.goods.entity.QWishCount;
import spharos.nu.goods.domain.goods.repository.GoodsRepositoryCustom;
import spharos.nu.goods.global.exception.CustomException;
import spharos.nu.goods.global.exception.errorcode.ErrorCode;

@RequiredArgsConstructor
public class GoodsRepositoryImpl implements GoodsRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public Page<GoodsSummaryDto> findAllGoods(Long categoryPk, boolean isTradingOnly, Pageable pageable) {
		QGoods goods = QGoods.goods;
		QViewsCount viewsCount = QViewsCount.viewsCount;
		QWishCount wishCount = QWishCount.wishCount;
		QBiddingCount biddingCount = QBiddingCount.biddingCount;
		QImage image = QImage.image;

		BooleanBuilder whereClause = new BooleanBuilder();
		whereClause
			.and(goods.categoryId.eq(categoryPk))
			.and(goods.isDelete.eq(false));

		if (isTradingOnly) {
			// true 일 때 tradingStatus 가 0
			whereClause.and(goods.tradingStatus.eq((byte)0));
		}

		System.out.println("거래중만보기" + isTradingOnly);
		System.out.println("카테고리id" + categoryPk);
		Long totalCount = getTotalCount(goods, whereClause);
		List<GoodsSummaryDto> content = getContent(goods, image, viewsCount, wishCount, biddingCount, whereClause,
			pageable);
		System.out.println("content" + content);
		System.out.println("count" + totalCount);
		return new PageImpl<>(content, pageable, totalCount);
	}

	@Override
	public Page<GoodsInfoDto> findAllGoods(byte statusNum, Pageable pageable) {

		QGoods goods = QGoods.goods;
		QImage image = QImage.image;

		List<GoodsInfoDto> goodsList = queryFactory
			.select(new QGoodsInfoDto(goods.code, image.url, goods.name, goods.minPrice, goods.tradingStatus)).from(goods)
			.join(image).on(goods.code.eq(image.code))
			.where(image.index.eq(0))
			.where(tradingStatusEq(statusNum))
			.orderBy(goods.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(goods.count())
			.from(goods)
			.where(tradingStatusEq(statusNum))
			.fetchOne();

		long totalCount = total != null ? total : 0;

		return new PageImpl<>(goodsList, pageable, totalCount);
	}

	private Long getTotalCount(QGoods goods, BooleanBuilder whereClause) {

		return queryFactory.select(goods.count()).from(goods).where(whereClause).fetchOne();
	}

	private <T> List<GoodsSummaryDto> getContent(QGoods goods, QImage image, QViewsCount viewsCount,
		QWishCount wishCount, QBiddingCount biddingCount, BooleanBuilder whereClause, Pageable pageable) {

		JPAQuery<GoodsSummaryDto> query = queryFactory.select(
				new QGoodsSummaryDto(goods.code, image.url, goods.name, goods.minPrice, goods.closedAt, viewsCount.count,
					wishCount.count, biddingCount.count, goods.tradingStatus)).from(goods)
			.join(image).on(goods.code.eq(image.code))
			.join(viewsCount).on(goods.code.eq(viewsCount.code))
			.join(wishCount).on(goods.code.eq(wishCount.code))
			.join(biddingCount).on(goods.code.eq(biddingCount.code))
			.where(image.index.eq(0))
			.where(whereClause);

		if (pageable.getSort().isSorted()) {
			Sort.Order order = pageable.getSort().iterator().next();
			PathBuilder<Goods> goodsPath = new PathBuilder<>(goods.getType(), goods.getMetadata());
			Order direction = Sort.Direction.ASC.equals(order.getDirection()) ? Order.ASC : Order.DESC;

			OrderSpecifier<?> orderSpecifier = switch (order.getProperty()) {
				case "createdAt", "closedAt" ->
					new OrderSpecifier<>(direction, goodsPath.getDateTime(order.getProperty(),
						LocalDateTime.class));
				case "viewsCount" ->
					new OrderSpecifier<>(direction, viewsCount.count);
				case "wishCount" ->
					new OrderSpecifier<>(direction, wishCount.count);
				case "biddingCount" ->
					new OrderSpecifier<>(direction, biddingCount.count);
				default -> throw new CustomException(ErrorCode.INVALID_REQUEST_PARAM);
			};

			query.orderBy(orderSpecifier);
		}

		query.offset(pageable.getOffset());
		query.limit(pageable.getPageSize());

		return query.fetch();
	}

	private BooleanExpression tradingStatusEq(Byte statusNum) {
		return statusNum == null ? null : QGoods.goods.tradingStatus.eq(statusNum);
	}
}
