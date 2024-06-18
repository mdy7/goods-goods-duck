package spharos.nu.read.domain.goods.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.read.domain.goods.dto.event.CountEventDto;
import spharos.nu.read.domain.goods.dto.event.GoodsCreateEventDto;
import spharos.nu.read.domain.goods.dto.event.GoodsDeleteEventDto;
import spharos.nu.read.domain.goods.dto.event.GoodsDisableEventDto;
import spharos.nu.read.domain.goods.dto.event.GoodsStatusEventDto;
import spharos.nu.read.domain.goods.entity.Goods;
import spharos.nu.read.domain.goods.repository.ReadRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadService {

	private final ReadRepository readRepository;

	@Transactional /* 리드용 굿즈 생성 */
	public void createGoods(GoodsCreateEventDto goodsCreateEventDto) {
		log.info("goods-create-topic 에서 이벤트 수신");
		readRepository.save(
			Goods.builder()
				.goodsCode(goodsCreateEventDto.getGoodsCode())
				.categoryId(goodsCreateEventDto.getCategoryId())
				.sellerUuid(goodsCreateEventDto.getSellerUuid())
				.name(goodsCreateEventDto.getName())
				.minPrice(goodsCreateEventDto.getMinPrice())
				.description(goodsCreateEventDto.getDescription())
				.openedAt(goodsCreateEventDto.getOpenedAt())
				.closedAt(goodsCreateEventDto.getClosedAt())
				.wishTradeType(goodsCreateEventDto.getWishTradeType())
				.tradingStatus(goodsCreateEventDto.getTradingStatus())
				.isDisable(goodsCreateEventDto.getIsDisable())
				.createdAt(goodsCreateEventDto.getCreatedAt())
				.updatedAt(goodsCreateEventDto.getUpdatedAt())
				.tagList(goodsCreateEventDto.getTagList())
				.wishCount(0L)
				.bidCount(0L)
				.viewsCount(0L)
				.build()
		);
	}

	@Transactional /*리드용 굿즈 삭제*/
	public void deleteGoods(GoodsDeleteEventDto goodsDeleteEventDto) {
		log.info("goods-delete-topic 에서 이벤트 수신");
		Goods goods = readRepository.findByGoodsCode(goodsDeleteEventDto.getGoodsCode()).orElseThrow(()->new NoSuchElementException("삭제하려했으나, 존재하지 않는 굿즈입니다"));
		readRepository.delete(goods);
	}

	@Transactional /*리드용 굿즈 상태필드 업데이트*/
	public void updateGoodsStatus(GoodsStatusEventDto goodsStatusEventDto) {
		Goods goods = readRepository.findByGoodsCode(goodsStatusEventDto.getGoodsCode()).orElseThrow(()->new NoSuchElementException("상태를 업데이트하려 했으나, 존재하지 않는 굿즈입니다"));

		readRepository.save(
			Goods.builder()
				.id(goods.getId())
				.goodsCode(goods.getGoodsCode())
				.categoryId(goods.getCategoryId())
				.sellerUuid(goods.getSellerUuid())
				.name(goods.getName())
				.minPrice(goods.getMinPrice())
				.description(goods.getDescription())
				.openedAt(goods.getOpenedAt())
				.closedAt(goods.getClosedAt())
				.wishTradeType(goods.getWishTradeType())
				.tradingStatus(goodsStatusEventDto.getTradingStatus()) //상태 업데이트
				.isDisable(goods.getIsDisable())
				.createdAt(goods.getCreatedAt())
				.updatedAt(goods.getUpdatedAt())
				.tagList(goods.getTagList())
				.wishCount(goods.getWishCount())
				.bidCount(goods.getBidCount())
				.viewsCount(goods.getViewsCount())
				.build()
		);
	}

	@Transactional /*리드용 굿즈 숨김필드 업데이트*/
	public void updateGoodsDisable(GoodsDisableEventDto goodsDisableEventDto) {
		Goods goods = readRepository.findByGoodsCode(goodsDisableEventDto.getGoodsCode()).orElseThrow(()->new NoSuchElementException("숨김하려했으나, 존재하지 않는 굿즈입니다"));

		readRepository.save(
			Goods.builder()
				.id(goods.getId())
				.goodsCode(goods.getGoodsCode())
				.categoryId(goods.getCategoryId())
				.sellerUuid(goods.getSellerUuid())
				.name(goods.getName())
				.minPrice(goods.getMinPrice())
				.description(goods.getDescription())
				.openedAt(goods.getOpenedAt())
				.closedAt(goods.getClosedAt())
				.wishTradeType(goods.getWishTradeType())
				.tradingStatus(goods.getTradingStatus())
				.isDisable(goodsDisableEventDto.getIsDisable()) //숨김 업데이트
				.createdAt(goods.getCreatedAt())
				.updatedAt(goods.getUpdatedAt())
				.tagList(goods.getTagList())
				.wishCount(goods.getWishCount())
				.bidCount(goods.getBidCount())
				.viewsCount(goods.getViewsCount())
				.build()
		);
	}

	@Transactional /*리드용 굿즈 좋아요수 업데이트*/
	public void updateWishCount(CountEventDto countEventDto) {
		Goods goods = readRepository.findByGoodsCode(countEventDto.getGoodsCode()).orElseThrow(()->new NoSuchElementException("좋아요를 하기엔 존재하지 않는 굿즈입니다"));

		readRepository.save(
			Goods.builder()
				.id(goods.getId())
				.goodsCode(goods.getGoodsCode())
				.categoryId(goods.getCategoryId())
				.sellerUuid(goods.getSellerUuid())
				.name(goods.getName())
				.minPrice(goods.getMinPrice())
				.description(goods.getDescription())
				.openedAt(goods.getOpenedAt())
				.closedAt(goods.getClosedAt())
				.wishTradeType(goods.getWishTradeType())
				.tradingStatus(goods.getTradingStatus())
				.isDisable(goods.getIsDisable())
				.createdAt(goods.getCreatedAt())
				.updatedAt(goods.getUpdatedAt())
				.tagList(goods.getTagList())
				.wishCount(countEventDto.getCount())  //좋아요수 업데이트
				.bidCount(goods.getBidCount())
				.viewsCount(goods.getViewsCount())
				.build()
		);
	}

	@Transactional /*리드용 굿즈 조회수 업데이트*/
	public void updateViewsCount(CountEventDto countEventDto) {
		Goods goods = readRepository.findByGoodsCode(countEventDto.getGoodsCode()).orElseThrow(()->new NoSuchElementException("조회수를 올리기엔 존재하지 않는 굿즈입니다"));

		readRepository.save(
			Goods.builder()
				.id(goods.getId())
				.goodsCode(goods.getGoodsCode())
				.categoryId(goods.getCategoryId())
				.sellerUuid(goods.getSellerUuid())
				.name(goods.getName())
				.minPrice(goods.getMinPrice())
				.description(goods.getDescription())
				.openedAt(goods.getOpenedAt())
				.closedAt(goods.getClosedAt())
				.wishTradeType(goods.getWishTradeType())
				.tradingStatus(goods.getTradingStatus())
				.isDisable(goods.getIsDisable())
				.createdAt(goods.getCreatedAt())
				.updatedAt(goods.getUpdatedAt())
				.tagList(goods.getTagList())
				.wishCount(goods.getWishCount())
				.bidCount(goods.getBidCount())
				.viewsCount(countEventDto.getCount()) //조회수 업데이트
				.build()
		);
	}

	@Transactional /*리드용 굿즈 입찰수 업데이트*/
	public void updateBidCount(CountEventDto countEventDto) {
		Goods goods = readRepository.findByGoodsCode(countEventDto.getGoodsCode()).orElseThrow(()->new NoSuchElementException("입찰수 올리기엔 존재하지 않는 굿즈입니다"));

		readRepository.save(
			Goods.builder()
				.id(goods.getId())
				.goodsCode(goods.getGoodsCode())
				.categoryId(goods.getCategoryId())
				.sellerUuid(goods.getSellerUuid())
				.name(goods.getName())
				.minPrice(goods.getMinPrice())
				.description(goods.getDescription())
				.openedAt(goods.getOpenedAt())
				.closedAt(goods.getClosedAt())
				.wishTradeType(goods.getWishTradeType())
				.tradingStatus(goods.getTradingStatus())
				.isDisable(goods.getIsDisable())
				.createdAt(goods.getCreatedAt())
				.updatedAt(goods.getUpdatedAt())
				.tagList(goods.getTagList())
				.wishCount(goods.getWishCount())
				.bidCount(countEventDto.getCount()) // 입찰 수 업데이트
				.viewsCount(goods.getViewsCount())
				.build()
		);
	}
}
