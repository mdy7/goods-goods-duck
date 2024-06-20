package spharos.nu.aggregation.domain.aggregation.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.aggregation.domain.aggregation.dto.event.GoodsCreateEventDto;
import spharos.nu.aggregation.domain.aggregation.dto.event.GoodsDeleteEventDto;
import spharos.nu.aggregation.domain.aggregation.service.AggregationService;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
	private final AggregationService aggregationService;

	@KafkaListener(topics = "goods-create-topic", containerFactory = "goodsCreateEventListener")
	public void goodsCreateEventListener(GoodsCreateEventDto goodsCreateEventDto) {
		aggregationService.createAggregation(goodsCreateEventDto);
	}

	@KafkaListener(topics = "goods-delete-topic", containerFactory = "goodsDeleteEventListener")
	public void goodsDeleteEventListener(GoodsDeleteEventDto goodsDeleteEventDto) {
		aggregationService.deleteAggregation(goodsDeleteEventDto);
	}

}
