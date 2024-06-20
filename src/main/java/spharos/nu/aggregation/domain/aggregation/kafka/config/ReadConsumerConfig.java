package spharos.nu.aggregation.domain.aggregation.kafka.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import lombok.RequiredArgsConstructor;
import spharos.nu.aggregation.domain.aggregation.dto.event.GoodsCreateEventDto;
import spharos.nu.aggregation.domain.aggregation.dto.event.GoodsDeleteEventDto;

@Configuration
@RequiredArgsConstructor
public class ReadConsumerConfig {
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;

	@Value("${spring.kafka.api-key}")
	private String apiKey;

	@Value("${spring.kafka.api-secret}")
	private String apiSecret;

	@Bean
	public Map<String,Object> aggregationConsumerConfigs() {
		return CommonJsonDeserializer.getStringObjectMap(bootstrapServer,apiKey,apiSecret);
	}

	/* 굿즈생성이벤트 리스너*/
	@Bean
	public ConsumerFactory<String, GoodsCreateEventDto> goodsCreateEventDtoConsumerFactory(){
		return new DefaultKafkaConsumerFactory<>(aggregationConsumerConfigs());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, GoodsCreateEventDto> goodsCreateEventListener() {
		ConcurrentKafkaListenerContainerFactory<String,GoodsCreateEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(goodsCreateEventDtoConsumerFactory());
		return factory;
	}

	/* 굿즈삭제이벤트 리스너*/
	@Bean
	public ConsumerFactory<String, GoodsDeleteEventDto> goodsDeleteEventDtoConsumerFactory(){
		return new DefaultKafkaConsumerFactory<>(aggregationConsumerConfigs());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, GoodsDeleteEventDto> goodsDeleteEventListener() {
		ConcurrentKafkaListenerContainerFactory<String,GoodsDeleteEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(goodsDeleteEventDtoConsumerFactory());
		return factory;
	}

	@Bean
	public StringJsonMessageConverter jsonMessageConverter() {
		return new StringJsonMessageConverter();
	}
}
