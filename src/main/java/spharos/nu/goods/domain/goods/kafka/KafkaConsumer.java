package spharos.nu.goods.domain.goods.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.goods.domain.goods.dto.event.TradingCompleteEventDto;
import spharos.nu.goods.domain.goods.service.GoodsService;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

	private final GoodsService goodsService;

	@KafkaListener(topics = "trading-complete-topic", containerFactory = "tradingCompleteEventListener")
	public void tradingCompleteEventListener(TradingCompleteEventDto tradingCompleteEventDto) {

		goodsService.updateTradingStatus(tradingCompleteEventDto);
	}
}
