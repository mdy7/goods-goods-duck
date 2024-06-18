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
import spharos.nu.goods.domain.bid.entity.QWinningBid;
import spharos.nu.goods.domain.bid.repository.WinningBidRepositoryCustom;
import spharos.nu.goods.domain.goods.entity.QGoods;

@RequiredArgsConstructor
public class WinningBidRepositoryImpl implements WinningBidRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<BidGoodsCodeDto> findAllGoods(String uuid, Pageable pageable, byte status) {

		QWinningBid winningBid = QWinningBid.winningBid;
		QGoods goods = QGoods.goods;

		List<BidGoodsCodeDto> goodsList = queryFactory
			.selectDistinct(new QBidGoodsCodeDto(winningBid.goodsCode))
			.from(winningBid)
			.join(goods).on(winningBid.goodsCode.eq(goods.goodsCode))
			.where(winningBid.bidderUuid.eq(uuid), tradingStatusEq(status))
			.orderBy(winningBid.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.selectDistinct(winningBid.goodsCode.count())
			.from(winningBid)
			.join(goods).on(winningBid.goodsCode.eq(goods.goodsCode))
			.where(winningBid.bidderUuid.eq(uuid), tradingStatusEq(status))
			.fetchOne();

		long totalCount = total != null ? total : 0;

		return new PageImpl<>(goodsList, pageable, totalCount);
	}

	private BooleanExpression tradingStatusEq(Byte status) {

		QGoods goods = QGoods.goods;

		if (status == null) {
			return null; // status가 null인 경우 조건을 추가하지 않음
		}

		// 경매완료탭은 status가 2 또는 3인 상품을 보여줌
		if (status == 2 || status == 3) {
			return goods.tradingStatus.eq((byte)2).or(goods.tradingStatus.eq((byte)3));
		}

		return goods.tradingStatus.eq(status);
	}
}

