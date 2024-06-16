package spharos.nu.auth.domain.auth.kafka.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import spharos.nu.auth.domain.auth.dto.event.JoinEventDto;

@Configuration
public class AuthProducerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;

	@Value("${spring.kafka.api-key}")
	private String apiKey;

	@Value("${spring.kafka.api-secret}")
	private String apiSecret;

	@Bean
	public Map<String, Object> authProducerConfigs() {
		return AuthJsonSerializer.getStringObjectMap(bootstrapServer, apiKey, apiSecret);
	}

	@Bean
	public ProducerFactory<String, JoinEventDto> joinEventDtoProducerFactory() {
		return new DefaultKafkaProducerFactory<>(authProducerConfigs());
	}

	@Bean
	public KafkaTemplate<String, JoinEventDto> joinEventDtoKafkaTemplate() {
		return new KafkaTemplate<>(joinEventDtoProducerFactory());
	}
}
