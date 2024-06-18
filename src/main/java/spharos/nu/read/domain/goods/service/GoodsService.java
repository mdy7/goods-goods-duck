package spharos.nu.read.domain.goods.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.read.domain.goods.dto.response.GoodsSummaryDto;
import spharos.nu.read.domain.goods.entity.Goods;
import spharos.nu.read.domain.goods.repository.ReadRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoodsService {

	private final ReadRepository readRepository;

	/**
	 * 상품 요약 조회
	 * @param goodsCode
	 */
	public GoodsSummaryDto goodsSummaryGet(String goodsCode) {

		Goods goods = readRepository.findByGoodsCode(goodsCode).orElseThrow();

		return GoodsSummaryDto.builder()
			.goodsCode(goods.getGoodsCode())
			.goodsName(goods.getName())
			.minPrice(goods.getMinPrice())
			.openedAt(goods.getOpenedAt())
			.closedAt(goods.getClosedAt())
			.tradingStatus(goods.getTradingStatus())
			.build();
	}
}
