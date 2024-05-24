package spharos.nu.goods.domain.goods.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spharos.nu.goods.domain.goods.dto.GoodsAllListDto;
import spharos.nu.goods.domain.goods.dto.GoodsCreateDto;
import spharos.nu.goods.domain.goods.dto.GoodsReadDto;
import spharos.nu.goods.domain.goods.dto.GoodsSummaryDto;
import spharos.nu.goods.domain.goods.entity.Goods;
import spharos.nu.goods.domain.goods.entity.Image;
import spharos.nu.goods.domain.goods.entity.Tag;
import spharos.nu.goods.domain.goods.repository.GoodsRepository;
import spharos.nu.goods.domain.goods.repository.ImageRepository;
import spharos.nu.goods.domain.goods.repository.TagRepository;
import spharos.nu.goods.domain.goods.client.BidServiceClient;
import spharos.nu.goods.global.apiresponse.ApiResponse;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoodsService {

	private final GoodsRepository goodsRepository;
	private final TagRepository tagRepository;
	private final ImageRepository imageRepository;
	private final BidServiceClient bidServiceClient;
	private final TaskScheduler taskScheduler;

	public GoodsAllListDto goodsAllRead(Long categoryPk, boolean isTradingOnly, Pageable pageable) {
		Page<GoodsSummaryDto> goodsPage = goodsRepository.findAllGoods(categoryPk,isTradingOnly,pageable);
		return GoodsAllListDto.builder()
			.maxPage(goodsPage.getTotalPages())
			.nowPage(goodsPage.getNumber())
			.totalCount(goodsPage.getTotalElements())
			.isLast(goodsPage.isLast())
			.goodsList(goodsPage.getContent())
			.build();
	}

	public String goodsCreate(GoodsCreateDto goodsCreateDto) {

		String code = createCode(goodsCreateDto);
		LocalDateTime openedAt = goodsCreateDto.getOpenedAt();

		Goods goods = Goods.builder()
			.name(goodsCreateDto.getGoodsName())
			.description(goodsCreateDto.getDescription())
			.categoryId(goodsCreateDto.getCategoryId())
			.minPrice(goodsCreateDto.getMinPrice())
			.openedAt(openedAt)
			.durationTime(goodsCreateDto.getDurationTime())
			.wishTradeType(goodsCreateDto.getWishTradeType())
			.uuid("111111")
			.code(code)
			.closedAt(openedAt.plusHours(goodsCreateDto.getDurationTime()))
			.build();

		//굿즈 저장
		Goods savedGoods = goodsRepository.save(goods);

		// 입찰 종료시간에 스케줄링 걸기
		scheduleCloseGoods(savedGoods);

		//태그 저장
		goodsCreateDto.getTags().forEach((tag) ->
			tagRepository.save(Tag.builder()
				.name(tag)
				.code(code)
				.build())
		);

		//이미지 저장
		IntStream.range(0, goodsCreateDto.getImageUrls().size())
			.forEach(index -> {
				String imageUrl = goodsCreateDto.getImageUrls().get(index);
				imageRepository.save(Image.builder()
					.url(imageUrl)
					.code(code)
					.index(index)
					.build());
			});

		return savedGoods.getCode();
	}

	private String createCode(GoodsCreateDto goodsCreateDto) {
		/* 상품코드 생성로직
		임시로 '카테고리 pk + (굿즈테이블 마지막 pk + 1)'로 가정함
		*/
		Optional<Goods> optionalGoods = goodsRepository.findFirstByOrderByIdDesc();
		Long lastGoodsId = optionalGoods.map(Goods::getId).orElse(1L);

		return goodsCreateDto.getCategoryId() + String.valueOf(lastGoodsId);
	}

	public GoodsReadDto goodsRead(String code) {
		Goods goods = goodsRepository.findOneByCode(code).orElseThrow();

		List<String> tags = tagRepository.findAllByCode(code).stream().map(Tag::getName).toList();
		List<String> imageUrls = imageRepository.findAllByCode(code).stream().map(Image::getUrl).toList();

		return GoodsReadDto.builder()
			.tradingStatus(goods.getTradingStatus())
			.goodsName(goods.getName())
			.description(goods.getDescription())
			.minPrice(goods.getMinPrice())
			.openedAt(goods.getOpenedAt())
			.closedAt(goods.getClosedAt())
			.durationTime(goods.getDurationTime())
			.wishTradeType(goods.getWishTradeType())
			.tags(tags)
			.imageUrls(imageUrls)
			.winningPrice(goods.getWinningPrice())
			.build();
	}

	@Transactional
	public Void goodsDelete(String code) {
		goodsRepository.deleteByCode(code);
		tagRepository.deleteAllByCode(code);
		imageRepository.deleteAllByCode(code);
		return null;
	}

	@Transactional
	public Void goodsDisable(String code) {
		Goods goods = goodsRepository.findOneByCode(code).orElseThrow();

		Goods updatedGoods = Goods.builder()
			.id(goods.getId())
			.name(goods.getName())
			.description(goods.getDescription())
			.categoryId(goods.getCategoryId())
			.minPrice(goods.getMinPrice())
			.openedAt(goods.getOpenedAt())
			.durationTime(goods.getDurationTime())
			.wishTradeType(goods.getWishTradeType())
			.uuid("111111")
			.code(goods.getCode())
			.closedAt(goods.getClosedAt())
			.winningPrice(goods.getWinningPrice())
			.tradingStatus((byte)2) //거래무산으로 변경
			.isDelete(true) //삭제여부 true로 변경
			.build();

		goodsRepository.save(updatedGoods);
		return null;
	}

	// 입찰 종료시간에 스케줄링
	private void scheduleCloseGoods(Goods goods) {
		taskScheduler.schedule(() -> CloseGoods(goods), Timestamp.valueOf(goods.getClosedAt()));
	}

	@Transactional
	public void CloseGoods(Goods goods) {
		log.info("(상품 코드: {}) 입찰 종료 ", goods.getCode());

		ResponseEntity<ApiResponse> response = bidServiceClient.selectWinningBid(goods.getCode(), goods.getClosedAt());

		Long winningBidPrice = null;

		if(response.getBody().getResult() != null){
			winningBidPrice = Long.valueOf((Integer)response.getBody().getResult());
		}

		Goods updatedGoods = Goods.builder()
			.id(goods.getId())
			.name(goods.getName())
			.description(goods.getDescription())
			.categoryId(goods.getCategoryId())
			.minPrice(goods.getMinPrice())
			.openedAt(goods.getOpenedAt())
			.durationTime(goods.getDurationTime())
			.wishTradeType(goods.getWishTradeType())
			.uuid(goods.getUuid())
			.code(goods.getCode())
			.closedAt(goods.getClosedAt())
			.winningPrice(winningBidPrice)
			.tradingStatus((byte)2)
			.isDelete(false)
			.build();

		Goods uppdateGoods = goodsRepository.save(updatedGoods);

		log.info("(상품 코드: {}) 낙찰가: {}",  uppdateGoods.getCode(), uppdateGoods.getWinningPrice());
	}
}
