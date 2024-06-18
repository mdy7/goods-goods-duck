package spharos.nu.read.domain.goods.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.read.domain.goods.dto.response.GoodsSellResponseDto;
import spharos.nu.read.domain.goods.dto.response.GoodsSummaryDto;
import spharos.nu.read.domain.goods.entity.Goods;
import spharos.nu.read.domain.goods.repository.ReadRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

	private final ReadRepository readRepository;

	public GoodsSellResponseDto sellGoodsGet(String uuid, Pageable pageable, byte status) {

		Boolean isDisable = false;

		Page<Goods> goodsSummaryPage = readRepository.findGoodsBySellerUuidAndTradingStatusAndIsDisableOrderByCreatedAtDesc(
			uuid, pageable, status, isDisable);

		List<GoodsSummaryDto> goodsSummaryList = goodsSummaryPage.getContent().stream()
			.map(goods -> GoodsSummaryDto.builder()
				.goodsCode(goods.getGoodsCode())
				.goodsName(goods.getName())
				.minPrice(goods.getMinPrice())
				.openedAt(goods.getOpenedAt())
				.closedAt(goods.getClosedAt())
				.tradingStatus(goods.getTradingStatus())
				.build())
			.toList();

		return GoodsSellResponseDto.builder()
			.totalCount(goodsSummaryPage.getTotalElements())
			.nowPage(goodsSummaryPage.getNumber())
			.maxPage(goodsSummaryPage.getTotalPages())
			.isLast(goodsSummaryPage.isLast())
			.goodsList(goodsSummaryList)
			.build();
	}
}
