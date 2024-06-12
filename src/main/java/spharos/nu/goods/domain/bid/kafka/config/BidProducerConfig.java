package spharos.nu.goods.domain.bid.kafka.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import spharos.nu.goods.domain.bid.dto.event.WinningEventDto;

@Configuration
public class BidProducerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;

	@Bean
	public Map<String,Object> bidProducerConfigs() {
		return CommonJsonSerializer.getStringObjectMap(bootstrapServer);
	}

	@Bean
	public ProducerFactory<String, WinningEventDto> winningEventDtoProducerFactory() {
		return new DefaultKafkaProducerFactory<>(bidProducerConfigs());
	}

	@Bean
	public KafkaTemplate<String, WinningEventDto> winningEventDtoKafkaTemplate() {
		return new KafkaTemplate<>(winningEventDtoProducerFactory());
	}
}
