package spharos.nu.chat.domain.chat.kafka;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import lombok.RequiredArgsConstructor;
import spharos.nu.chat.domain.chat.dto.event.WinningEventDto;

@Configuration
@RequiredArgsConstructor
public class ChatConsumerConfig {
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;

	@Value("${spring.kafka.api-key}")
	private String apiKey;

	@Value("${spring.kafka.api-secret}")
	private String apiSecret;

	@Bean
	public Map<String,Object> chatConsumerConfigs() {
		return CommonJsonDeserializer.getStringObjectMap(bootstrapServer,apiKey,apiSecret);
	}

	@Bean
	public ConsumerFactory<String, WinningEventDto> winningEventDtoConsumerFactory(){
		return new DefaultKafkaConsumerFactory<>(chatConsumerConfigs());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, WinningEventDto> winningEventListener() {
		ConcurrentKafkaListenerContainerFactory<String,WinningEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(winningEventDtoConsumerFactory());
		return factory;
	}

	@Bean
	public StringJsonMessageConverter jsonMessageConverter() {
		return new StringJsonMessageConverter();
	}
}
