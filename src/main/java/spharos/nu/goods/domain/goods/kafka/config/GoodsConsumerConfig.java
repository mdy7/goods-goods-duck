package spharos.nu.goods.domain.goods.kafka.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import lombok.RequiredArgsConstructor;
import spharos.nu.goods.domain.goods.dto.event.TradingCompleteEventDto;

@Configuration
@RequiredArgsConstructor
public class GoodsConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;

	@Bean
	public Map<String, Object> goodsConsumerConfigs() {
		return CommonJsonDeserializer.getStringObjectMap(bootstrapServer);
	}

	@Bean
	public ConsumerFactory<String, TradingCompleteEventDto> tradingCompleteEventConsumerFactory() {
		return new DefaultKafkaConsumerFactory<>(goodsConsumerConfigs());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, TradingCompleteEventDto> tradingCompleteEventListener() {

		ConcurrentKafkaListenerContainerFactory<String, TradingCompleteEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(tradingCompleteEventConsumerFactory());

		return factory;
	}

	@Bean
	public StringJsonMessageConverter jsonMessageConverter() {
		return new StringJsonMessageConverter();
	}
}
