package spharos.nu.goods.domain.bid.repository.impl;

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

@RequiredArgsConstructor
public class BidRepositoryImpl implements BidRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<BidGoodsCodeDto> findAllGoods(String uuid, Pageable pageable, byte status) {

		QBid bid = QBid.bid;
		QGoods goods = QGoods.goods;

		List<BidGoodsCodeDto> goodsList = queryFactory
			.select(new QBidGoodsCodeDto(bid.goodsCode))
			.from(bid)
			.join(goods).on(bid.goodsCode.eq(goods.goodsCode))
			.where(bid.bidderUuid.eq(uuid), tradingStatusEq(status))
			.orderBy(bid.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(bid.count())
			.from(bid)
			.where(bid.bidderUuid.eq(uuid), tradingStatusEq(status))
			.fetchOne();

		long totalCount = total != null ? total : 0;

		return new PageImpl<>(goodsList, pageable, totalCount);
	}

	private BooleanExpression tradingStatusEq(Byte status) {
		return status == null ? null : QGoods.goods.tradingStatus.eq(status);
	}
}
