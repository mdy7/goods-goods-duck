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

	public GoodsSellResponseDto sellGoodsGet(String uuid, Pageable pageable, Byte status) {

		Boolean isDisable = false;

		Page<Goods> goodsPage;

		if (status == null) {
			goodsPage = readRepository.findGoodsBySellerUuidAndIsDisableOrderByCreatedAtDesc(uuid, pageable, isDisable);
		} else {
			goodsPage = readRepository.findGoodsBySellerUuidAndTradingStatusAndIsDisableOrderByCreatedAtDesc(uuid, pageable, status, isDisable);
		}

		List<GoodsSummaryDto> goodsSummaryList = goodsPage.getContent().stream()
			.map(goods -> GoodsSummaryDto.builder()
				.goodsCode(goods.getGoodsCode())
				.goodsName(goods.getName())
				.thumbnail(goods.getImageList() != null && !goods.getImageList().isEmpty() ? goods.getImageList().get(0) : null)
				.minPrice(goods.getMinPrice())
				.openedAt(goods.getOpenedAt())
				.closedAt(goods.getClosedAt())
				.tradingStatus(goods.getTradingStatus())
				.build())
			.toList();

		return GoodsSellResponseDto.builder()
			.totalCount(goodsPage.getTotalElements())
			.nowPage(goodsPage.getNumber())
			.maxPage(goodsPage.getTotalPages())
			.isLast(goodsPage.isLast())
			.goodsList(goodsSummaryList)
			.build();
	}
}
