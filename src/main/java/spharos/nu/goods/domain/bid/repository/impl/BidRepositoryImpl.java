package spharos.nu.goods.domain.bid.repository.impl;

import static com.querydsl.jpa.JPAExpressions.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import spharos.nu.goods.domain.bid.dto.response.BidGoodsCodeDto;
import spharos.nu.goods.domain.bid.dto.response.QBidGoodsCodeDto;
import spharos.nu.goods.domain.bid.entity.QBid;
import spharos.nu.goods.domain.bid.repository.BidRepositoryCustom;
import spharos.nu.goods.domain.goods.entity.QGoods;
import spharos.nu.goods.domain.goods.entity.QImage;

@RequiredArgsConstructor
public class BidRepositoryImpl implements BidRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<BidGoodsCodeDto> findAllGoods(String uuid, Pageable pageable, byte status) {

		QBid bid = QBid.bid;
		QGoods goods = QGoods.goods;
		QImage image = QImage.image;

		List<BidGoodsCodeDto> goodsList = queryFactory
			.select(new QBidGoodsCodeDto(
				bid.goodsCode,
				goods.name,
				select(bid.price.max())
					.from(bid)
					.where(bid.goodsCode.eq(goods.goodsCode), bid.bidderUuid.eq(uuid)),
				image.url
			))
			.distinct()
			.from(bid)
			.join(goods).on(bid.goodsCode.eq(goods.goodsCode))
			.join(image).on(bid.goodsCode.eq(image.goodsCode).and(image.index.eq(0)))
			.where(goods.isDisable.eq(false), bid.bidderUuid.eq(uuid), tradingStatusEq(status))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(bid.goodsCode.count())
			.distinct()
			.from(bid)
			.join(goods).on(bid.goodsCode.eq(goods.goodsCode))
			.where(goods.isDisable.eq(false), bid.bidderUuid.eq(uuid), tradingStatusEq(status))
			.fetchOne();

		long totalCount = total != null ? total : 0;

		return new PageImpl<>(goodsList, pageable, totalCount);
	}

	private BooleanExpression tradingStatusEq(Byte status) {

		QGoods goods = QGoods.goods;

		if (status == 9) {
			return null; // status가 null인 경우 조건을 추가하지 않음
		}

		// 경매완료탭은 status가 2 또는 3인 상품을 보여줌
		if (status == 2 || status == 3) {
			return goods.tradingStatus.eq((byte)2).or(goods.tradingStatus.eq((byte)3));
		}

		return goods.tradingStatus.eq(status);
	}
}
