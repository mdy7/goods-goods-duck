package spharos.nu.read.domain.goods.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.read.domain.goods.dto.response.GoodsSellResponseDto;
import spharos.nu.read.domain.goods.dto.response.GoodsSummaryDto;
import spharos.nu.read.domain.goods.repository.ReadRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

	private final ReadRepository readRepository;

	public GoodsSellResponseDto sellGoodsGet(String uuid, Pageable pageable, byte status) {

		Page<GoodsSummaryDto> goodsSummaryPage = readRepository.findGoodsBySellerUuidAndTradingStatusOrderByCreatedAtDesc(
			uuid, pageable, status);

		return GoodsSellResponseDto.builder()
			.totalCount(goodsSummaryPage.getTotalElements())
			.nowPage(goodsSummaryPage.getNumber())
			.maxPage(goodsSummaryPage.getTotalPages())
			.isLast(goodsSummaryPage.isLast())
			.goodsList(goodsSummaryPage.getContent())
			.build();
	}
}
