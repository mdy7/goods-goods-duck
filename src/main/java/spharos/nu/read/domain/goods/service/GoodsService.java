package spharos.nu.read.domain.goods.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.read.domain.goods.dto.response.AllGoodsDto;
import spharos.nu.read.domain.goods.dto.response.GoodsDetailDto;
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
			.thumbnail(goods.getImageList() != null && !goods.getImageList().isEmpty() ? goods.getImageList().get(0) : null)
			.minPrice(goods.getMinPrice())
			.openedAt(goods.getOpenedAt())
			.closedAt(goods.getClosedAt())
			.tradingStatus(goods.getTradingStatus())
			.build();
	}

	public GoodsDetailDto getGoodsDetail(String goodsCode) {

		Goods goods = readRepository.findByGoodsCode(goodsCode).orElseThrow();

		return GoodsDetailDto.builder()
			.goodsCode(goods.getGoodsCode())
			.categoryId(goods.getCategoryId())
			.sellerUuid(goods.getSellerUuid())
			.name(goods.getName())
			.imageList(goods.getImageList())
			.tagList(goods.getTagList())
			.minPrice(goods.getMinPrice())
			.description(goods.getDescription())
			.openedAt(goods.getOpenedAt())
			.closedAt(goods.getClosedAt())
			.wishTradeType(goods.getWishTradeType())
			.tradingStatus(goods.getTradingStatus())
			.isDisable(goods.getIsDisable())
			.createdAt(goods.getCreatedAt())
			.updatedAt(goods.getUpdatedAt())
			.build();
	}

	public AllGoodsDto getAllGoods(Long categoryPk, boolean isTradingOnly, Pageable pageable) {

		/* isTradingOnly boolean 값에 따라 tradingStatus 상태 리스트 정의*/
		List<Byte> tradingStatus;
		if (isTradingOnly) {
			tradingStatus = Arrays.asList((byte)0, (byte)1);
		} else {
			tradingStatus = Arrays.asList((byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5);
		}

		Page<Goods> goodsPage = readRepository.findByCategoryIdAndTradingStatusInAndIsDisable(categoryPk, tradingStatus,
			false, pageable);

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

		return AllGoodsDto.builder()
			.maxPage(goodsPage.getTotalPages())
			.nowPage(goodsPage.getNumber())
			.totalCount(goodsPage.getTotalElements())
			.isLast(goodsPage.isLast())
			.goodsList(goodsSummaryList)
			.build();
	}

	public List<GoodsSummaryDto> getNowTradingGoods(Long categoryId, Pageable pageable) {
		LocalDateTime now = LocalDateTime.now();

		List<Goods> goodsList = readRepository.findByCategoryIdAndOpenedAtBeforeAndClosedAtAfter(categoryId, now, now,
			pageable);

		return goodsList.stream().map(goods -> GoodsSummaryDto.builder()
			.goodsCode(goods.getGoodsCode())
			.goodsName(goods.getName())
			.thumbnail(goods.getImageList() != null && !goods.getImageList().isEmpty() ? goods.getImageList().get(0) : null)
			.minPrice(goods.getMinPrice())
			.openedAt(goods.getOpenedAt())
			.closedAt(goods.getClosedAt())
			.tradingStatus(goods.getTradingStatus())
			.build()).toList();
	}

	public List<GoodsSummaryDto> getComingSoonGoods(Long categoryId, Pageable pageable) {
		LocalDate today = LocalDate.now();
		LocalDateTime startOfTomorrow = today.plusDays(1).atStartOfDay();
		LocalDateTime endOfTomorrow = today.plusDays(1).atTime(LocalTime.MAX);

		List<Goods> goodsList = readRepository.findByCategoryIdAndOpenedAtBetween(categoryId, startOfTomorrow,
			endOfTomorrow, pageable);

		return goodsList.stream().map(goods -> GoodsSummaryDto.builder()
			.goodsCode(goods.getGoodsCode())
			.goodsName(goods.getName())
			.thumbnail(goods.getImageList() != null && !goods.getImageList().isEmpty() ? goods.getImageList().get(0) : null)
			.minPrice(goods.getMinPrice())
			.openedAt(goods.getOpenedAt())
			.closedAt(goods.getClosedAt())
			.tradingStatus(goods.getTradingStatus())
			.build()).toList();
	}
}
