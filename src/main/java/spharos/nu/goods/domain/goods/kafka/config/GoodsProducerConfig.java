package spharos.nu.goods.domain.goods.kafka.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import lombok.Getter;
import spharos.nu.goods.domain.goods.dto.event.CloseEventDto;
import spharos.nu.goods.domain.goods.dto.event.OpenEventDto;

@Configuration
public class GoodsProducerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;

	@Value("${spring.kafka.api-key}")
	private String apiKey;

	@Value("${spring.kafka.api-secret}")
	private String apiSecret;

	@Bean
	public Map<String, Object> goodsProducerConfigs() {
		return CommonJsonSerializer.getStringObjectMap(bootstrapServer,apiKey,apiSecret);
	}

	@Bean
	public ProducerFactory<String, OpenEventDto> openEventDtoProducerFactory() {
		return new DefaultKafkaProducerFactory<>(goodsProducerConfigs());
	}

	@Bean
	public ProducerFactory<String, CloseEventDto> closeEventDtoProducerFactory() {
		return new DefaultKafkaProducerFactory<>(goodsProducerConfigs());
	}

	@Bean
	public KafkaTemplate<String, OpenEventDto> openEventDtoKafkaTemplate() {
		return new KafkaTemplate<>(openEventDtoProducerFactory());
	}

	@Bean
	public KafkaTemplate<String, CloseEventDto> closeEventDtoKafkaTemplate() {
		return new KafkaTemplate<>(closeEventDtoProducerFactory());
	}
}
