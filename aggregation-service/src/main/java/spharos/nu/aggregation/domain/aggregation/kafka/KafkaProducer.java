package spharos.nu.aggregation.domain.aggregation.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.aggregation.domain.aggregation.dto.event.CountEventDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

	private final KafkaTemplate<String, CountEventDto> countEventKafkaTemplate;

	public void sendWishCountEvent(CountEventDto countEventDto) {
		countEventKafkaTemplate.send("wish-count-topic",countEventDto);
	}

	public void sendViewsCountEvent(CountEventDto countEventDto) {
		countEventKafkaTemplate.send("views-count-topic",countEventDto);
	};

	public void sendBidCountEvent(CountEventDto countEventDto) {
		countEventKafkaTemplate.send("bid-count-topic",countEventDto);
	};
}
