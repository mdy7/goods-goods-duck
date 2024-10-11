package spharos.nu.member.domain.member.kafka.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import lombok.RequiredArgsConstructor;
import spharos.nu.member.domain.member.dto.event.JoinEventDto;
import spharos.nu.member.domain.member.dto.event.MemberScoreEventDto;

@Configuration
@RequiredArgsConstructor
public class MemberConsumerConfig {
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;


	@Bean
	public Map<String, Object> memberConsumerConfigs() {
		return MemberJsonDeserializer.getStringObjectMap(bootstrapServer);
	}

	@Bean
	public ConsumerFactory<String, JoinEventDto> joinEventDtoConsumerFactory() {
		return new DefaultKafkaConsumerFactory<>(memberConsumerConfigs());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, JoinEventDto> joinEventListener() {
		ConcurrentKafkaListenerContainerFactory<String, JoinEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(joinEventDtoConsumerFactory());

		return factory;
	}

	@Bean
	public ConsumerFactory<String, MemberScoreEventDto> memberScoreEventDtoConsumerFactory() {
		return new DefaultKafkaConsumerFactory<>(memberConsumerConfigs());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, MemberScoreEventDto> memberScoreEventListener() {

		ConcurrentKafkaListenerContainerFactory<String, MemberScoreEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(memberScoreEventDtoConsumerFactory());

		return factory;
	}

	@Bean
	public StringJsonMessageConverter jsonMessageConverter() {
		return new StringJsonMessageConverter();
	}

}
