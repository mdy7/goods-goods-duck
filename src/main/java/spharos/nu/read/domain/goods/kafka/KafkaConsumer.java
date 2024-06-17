package spharos.nu.read.domain.goods.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.read.domain.goods.dto.event.CountEventDto;
import spharos.nu.read.domain.goods.dto.event.GoodsCreateEventDto;
import spharos.nu.read.domain.goods.dto.event.GoodsDeleteEventDto;
import spharos.nu.read.domain.goods.dto.event.GoodsDisableEventDto;
import spharos.nu.read.domain.goods.dto.event.GoodsStatusEventDto;
import spharos.nu.read.domain.goods.service.ReadService;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
	private final ReadService readService;

	@KafkaListener(topics = "wish-count-topic", containerFactory = "countEventListener")
	public void wishCountEventListener(CountEventDto countEventDto) {
		readService.updateWishCount(countEventDto);
	}

	@KafkaListener(topics = "views-count-topic", containerFactory = "countEventListener")
	public void viewsCountEventListener(CountEventDto countEventDto) {
		readService.updateViewsCount(countEventDto);
	}

	@KafkaListener(topics = "bid-count-topic", containerFactory = "countEventListener")
	public void bidCountEventListener(CountEventDto countEventDto) {
		readService.updateBidCount(countEventDto);
	}

	@KafkaListener(topics = "goods-create-topic", containerFactory = "goodsCreateEventListener")
	public void goodsCreateEventListener(GoodsCreateEventDto goodsCreateEventDto) {
		readService.createGoods(goodsCreateEventDto);
	}

	@KafkaListener(topics = "goods-delete-topic", containerFactory = "goodsDeleteEventListener")
	public void goodsDeleteEventListener(GoodsDeleteEventDto goodsDeleteEventDto) {
		readService.deleteGoods(goodsDeleteEventDto);
	}

	@KafkaListener(topics = "goods-status-topic", containerFactory = "goodsStatusEventListener")
	public void goodsStatusEventListener(GoodsStatusEventDto goodsStatusEventDto) {
		readService.updateGoodsStatus(goodsStatusEventDto);
	}

	@KafkaListener(topics = "goods-disable-topic", containerFactory = "goodsDisableEventListener")
	public void goodsDisableEventListener(GoodsDisableEventDto goodsDisableEventDto) {
		readService.updateGoodsDisable(goodsDisableEventDto);
	}

}
