package spharos.nu.goods.domain.goods.kafka;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import spharos.nu.goods.domain.goods.dto.CloseEventDto;
import spharos.nu.goods.domain.goods.dto.OpenEventDto;

@Configuration
public class GoodsProducerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;

	@Bean
	public Map<String,Object> goodsProducerConfigs() {
		return CommonJsonSerializer.getStringObjectMap(bootstrapServer);
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
