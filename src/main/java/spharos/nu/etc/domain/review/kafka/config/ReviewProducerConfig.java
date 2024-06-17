package spharos.nu.etc.domain.review.kafka.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import spharos.nu.etc.domain.review.dto.event.MemberReviewEventDto;
import spharos.nu.etc.domain.review.dto.event.TradingCompleteEventDto;

@Configuration
public class ReviewProducerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;

	@Value("${spring.kafka.api-key}")
	private String apiKey;

	@Value("${spring.kafka.api-secret}")
	private String apiSecret;

	@Bean
	public Map<String, Object> reviewProducerConfigs() {
		return CommonJsonSerializer.getStringObjectMap(bootstrapServer, apiKey, apiSecret);
	}

	// 회원 점수 전송
	@Bean
	public ProducerFactory<String, MemberReviewEventDto> MemberReviewEventProducerFactory() {
		return new DefaultKafkaProducerFactory<>(reviewProducerConfigs());
	}

	@Bean
	public KafkaTemplate<String, MemberReviewEventDto> MemberReviewEventKafkaTemplate() {
		return new KafkaTemplate<>(MemberReviewEventProducerFactory());
	}

	// 거래 상태 전송
	@Bean
	public ProducerFactory<String, TradingCompleteEventDto> tradingCompleteEventProducerFactory() {
		return new DefaultKafkaProducerFactory<>(reviewProducerConfigs());
	}

	@Bean
	public KafkaTemplate<String, TradingCompleteEventDto> tradingCompleteEventKafkaTemplate() {
		return new KafkaTemplate<>(tradingCompleteEventProducerFactory());
	}
}
