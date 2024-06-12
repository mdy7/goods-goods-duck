package spharos.nu.etc.domain.review.kafka.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import spharos.nu.etc.domain.review.dto.event.MemberScoreEventDto;

@Configuration
public class ReviewProducerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;

	@Bean
	public Map<String, Object> reviewProducerConfigs() {
		return CommonJsonSerializer.getStringObjectMap(bootstrapServer);
	}

	// 회원 점수 전송
	@Bean
	public ProducerFactory<String, MemberScoreEventDto> MemberScoreEventProducerFactory() {
		return new DefaultKafkaProducerFactory<>(reviewProducerConfigs());
	}

	@Bean
	public KafkaTemplate<String, MemberScoreEventDto> MemberScoreEventKafkaTemplate() {
		return new KafkaTemplate<>(MemberScoreEventProducerFactory());
	}
}
