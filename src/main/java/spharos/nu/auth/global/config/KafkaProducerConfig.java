package spharos.nu.auth.global.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import spharos.nu.auth.domain.auth.dto.KafkaUserCreatedDto;

@Configuration
@EnableKafka
public class KafkaProducerConfig {
	@Value("${spring.kafka.bootstrap-server}")
	private String bootstrapServer;

	@Bean
	public ProducerFactory<Long, KafkaUserCreatedDto> factory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		props.put(JsonSerializer.TYPE_MAPPINGS, "spharos.nu.auth.domain.auth.dto.KafkaUserCreatedDto=spharos.nu.auth.domain.auth.dto.KafkaUserCreatedDto");

		// 롤백 관련 설정
		props.put(ProducerConfig.RETRIES_CONFIG, 3); // 재시도 횟수
		props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 500); // 재시도 간 대기 시간(ms)
		props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1); // 동시 처리 요청 수
		props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 30000); // 메시지 전송 시간 초과(ms)

		return new DefaultKafkaProducerFactory<>(props);
	}

	@Bean
	public KafkaTemplate<Long, KafkaUserCreatedDto> kafkaTemplate() {
		return new KafkaTemplate<>(factory());
	}
}
