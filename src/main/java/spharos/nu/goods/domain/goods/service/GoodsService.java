package spharos.nu.goods.domain.goods.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.goods.domain.bid.service.BidService;
import spharos.nu.goods.domain.goods.dto.event.OpenEventDto;
import spharos.nu.goods.domain.goods.dto.event.TradingCompleteEventDto;
import spharos.nu.goods.domain.goods.dto.request.GoodsCreateDto;
import spharos.nu.goods.domain.goods.dto.request.ImageDto;
import spharos.nu.goods.domain.goods.dto.request.TagDto;
import spharos.nu.goods.domain.goods.dto.response.GoodsAllListDto;
import spharos.nu.goods.domain.goods.dto.response.GoodsCodeDto;
import spharos.nu.goods.domain.goods.dto.response.GoodsDetailDto;
import spharos.nu.goods.domain.goods.dto.response.GoodsSummaryDto;
import spharos.nu.goods.domain.goods.entity.Goods;
import spharos.nu.goods.domain.goods.entity.Image;
import spharos.nu.goods.domain.goods.entity.Tag;
import spharos.nu.goods.domain.goods.repository.GoodsRepository;
import spharos.nu.goods.domain.goods.repository.ImageRepository;
import spharos.nu.goods.domain.goods.repository.TagRepository;
import spharos.nu.goods.global.exception.CustomException;
import spharos.nu.goods.global.exception.errorcode.ErrorCode;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoodsService {

	private final GoodsRepository goodsRepository;
	private final TagRepository tagRepository;
	private final ImageRepository imageRepository;
	private final BidService bidService;
	private final TaskScheduler taskScheduler;
	private final KafkaTemplate<String, OpenEventDto> openEventDtoKafkaTemplate;

	public GoodsAllListDto goodsAllRead(Long categoryPk, boolean isTradingOnly, Pageable pageable) {
		Page<GoodsCodeDto> goodsPage = goodsRepository.findAllGoods(categoryPk, isTradingOnly, pageable);
		return GoodsAllListDto.builder()
			.maxPage(goodsPage.getTotalPages())
			.nowPage(goodsPage.getNumber())
			.totalCount(goodsPage.getTotalElements())
			.isLast(goodsPage.isLast())
			.goodsList(goodsPage.getContent())
			.build();
	}

	public String goodsCreate(String uuid, GoodsCreateDto goodsCreateDto) {

		String goodsCode = createCode(goodsCreateDto);

		Goods goods = Goods.builder()
			.name(goodsCreateDto.getGoodsName())
			.description(goodsCreateDto.getDescription())
			.categoryId(goodsCreateDto.getCategoryId())
			.minPrice(goodsCreateDto.getMinPrice())
			.openedAt(goodsCreateDto.getOpenedAt())
			.closedAt(goodsCreateDto.getClosedAt())
			.wishTradeType(goodsCreateDto.getWishTradeType())
			.sellerUuid(uuid)
			.goodsCode(goodsCode)
			.build();

		//굿즈 저장
		Goods savedGoods = goodsRepository.save(goods);

		//태그 저장
		goodsCreateDto.getTags().forEach((tag) ->
			tagRepository.save(Tag.builder()
				.name(tag.getName())
				.goodsCode(goodsCode)
				.build())
		);
		//이미지 저장
		goodsCreateDto.getImages().forEach((image) ->
			imageRepository.save(Image.builder()
				.url(image.getUrl())
				.index(image.getId())
				.goodsCode(goodsCode)
				.build())
		);

		// 경매 시작시간에 스케줄링 걸기
		scheduleOpenGoods(savedGoods);

		// 경매 종료시간에 스케줄링 걸기
		scheduleCloseGoods(savedGoods);

		return savedGoods.getGoodsCode();
	}

	private String createCode(GoodsCreateDto goodsCreateDto) {
		/* 상품코드 생성로직
		임시로 '카테고리 pk + (굿즈테이블 마지막 pk + 1)'로 가정함
		*/
		Optional<Goods> optionalGoods = goodsRepository.findFirstByOrderByIdDesc();
		Long lastGoodsId = optionalGoods.map(Goods::getId).orElse(1L);

		return goodsCreateDto.getCategoryId() + String.valueOf(lastGoodsId);
	}

	public GoodsDetailDto getGoodsDetail(String goodsCode) {
		Goods goods = goodsRepository.findOneByGoodsCode(goodsCode).orElseThrow();

		List<TagDto> tags = tagRepository.findAllByGoodsCode(goodsCode).stream()
			.map((tag) -> TagDto.builder().id(tag.getId()).name(tag.getName()).build()).toList();
		List<ImageDto> imageUrls = imageRepository.findAllByGoodsCode(goodsCode).stream()
			.map((image) -> ImageDto.builder().id(image.getIndex()).url(image.getUrl()).build()).toList();

		return GoodsDetailDto.builder()
			.tradingStatus(goods.getTradingStatus())
			.goodsName(goods.getName())
			.description(goods.getDescription())
			.minPrice(goods.getMinPrice())
			.openedAt(goods.getOpenedAt())
			.closedAt(goods.getClosedAt())
			.wishTradeType(goods.getWishTradeType())
			.tags(tags)
			.imageUrls(imageUrls)
			.build();
	}

	public GoodsSummaryDto getGoodsSummary(String goodsCode) {
		Goods goods = goodsRepository.findOneByGoodsCode(goodsCode).orElseThrow();
		String thumbnailUrl = imageRepository.findAllByGoodsCode(goodsCode).get(0).getUrl();

		return GoodsSummaryDto.builder()
			.thumbnailUrl(thumbnailUrl)
			.goodsName(goods.getName())
			.minPrice(goods.getMinPrice())
			.openedAt(goods.getOpenedAt())
			.closedAt(goods.getClosedAt())
			.tradingStatus(goods.getTradingStatus())
			.build();
	}

	@Transactional
	public Void goodsDelete(String goodsCode) {
		/*상태가 0이 아닌 경우 삭제 불가능*/
		Goods goods = goodsRepository.findOneByGoodsCode(goodsCode).orElseThrow();
		if (goods.getTradingStatus() != 0) {
			throw new CustomException(ErrorCode.FORBIDDEN_DELETE);
		}

		goodsRepository.deleteByGoodsCode(goodsCode);
		tagRepository.deleteAllByGoodsCode(goodsCode);
		imageRepository.deleteAllByGoodsCode(goodsCode);

		return null;
	}

	@Transactional
	public Void goodsDisable(String uuid, String goodsCode) {
		Goods goods = goodsRepository.findOneByGoodsCode(goodsCode).orElseThrow();

		Goods updatedGoods = Goods.builder()
			.id(goods.getId())
			.name(goods.getName())
			.description(goods.getDescription())
			.categoryId(goods.getCategoryId())
			.minPrice(goods.getMinPrice())
			.openedAt(goods.getOpenedAt())
			.wishTradeType(goods.getWishTradeType())
			.sellerUuid(uuid)
			.goodsCode(goods.getGoodsCode())
			.closedAt(goods.getClosedAt())
			.tradingStatus(goods.getTradingStatus())
			.isDisable(true) //숨김 여부 true 로 변경
			.build();

		goodsRepository.save(updatedGoods);
		return null;
	}

	// 경매 시작시간에 스케줄링
	private void scheduleOpenGoods(Goods goods) {
		taskScheduler.schedule(() -> OpenGoods(goods), Timestamp.valueOf(goods.getOpenedAt()));
	}

	@Transactional
	public void OpenGoods(Goods goods) {
		log.info("(상품 코드: {}) 경매 시작 ", goods.getGoodsCode());

		OpenEventDto openEventDto = OpenEventDto.builder()
			.goodsCode(goods.getGoodsCode())
			.openedAt(goods.getOpenedAt())
			.build();

		openEventDtoKafkaTemplate.send("goods-open-topic", openEventDto);
	}

	// 경매 종료시간에 스케줄링
	private void scheduleCloseGoods(Goods goods) {
		taskScheduler.schedule(() -> bidService.addWinningBid(goods), Timestamp.valueOf(goods.getClosedAt()));
	}

	public byte getGoodsTradingStatus(String goodsCode) {
		Goods goods = goodsRepository.findOneByGoodsCode(goodsCode).orElseThrow();

		return goods.getTradingStatus();
	}

	// 거래 완료 카프카 통신
	public void updateTradingStatus(TradingCompleteEventDto tradingCompleteEventDto) {

		log.info("거래상태 변경");
		Goods goods = goodsRepository.findOneByGoodsCode(tradingCompleteEventDto.getGoodsCode()).orElseThrow();

		goodsRepository.save(Goods.builder()
			.id(goods.getId())
			.categoryId(goods.getCategoryId())
			.sellerUuid(goods.getSellerUuid())
			.goodsCode(goods.getGoodsCode())
			.name(goods.getName())
			.minPrice(goods.getMinPrice())
			.deposit(goods.getDeposit())
			.description(goods.getDescription())
			.openedAt(goods.getOpenedAt())
			.closedAt(goods.getClosedAt())
			.wishTradeType(goods.getWishTradeType())
			.tradingStatus((byte) 3)
			.isDisable(goods.isDisable())
			.build());

		log.info("굿즈코드 {} : 거래 상태 변경 완료", tradingCompleteEventDto.getGoodsCode());
		log.info("상태코드 {} : 확인 완료", goods.getTradingStatus());

	}
}
