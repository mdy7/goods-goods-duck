package spharos.nu.aggregation.domain.aggregation.kafka.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import spharos.nu.aggregation.domain.aggregation.dto.event.CountEventDto;

@Configuration
public class AggregationProducerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;

	@Value("${spring.kafka.api-key}")
	private String apiKey;

	@Value("${spring.kafka.api-secret}")
	private String apiSecret;

	@Bean
	public Map<String, Object> aggregationProducerConfigs() {
		return CommonJsonSerializer.getStringObjectMap(bootstrapServer,apiKey,apiSecret);
	}
    /* 카운트 카프카템플릿*/
	@Bean
	public ProducerFactory<String, CountEventDto> countEventDtoProducerFactory() {
		return new DefaultKafkaProducerFactory<>(aggregationProducerConfigs());
	}
	@Bean
	public KafkaTemplate<String, CountEventDto> countEventDtoKafkaTemplate() {
		return new KafkaTemplate<>(countEventDtoProducerFactory());
	}

}
