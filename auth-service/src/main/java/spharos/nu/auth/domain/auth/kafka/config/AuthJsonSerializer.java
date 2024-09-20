package spharos.nu.auth.domain.auth.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class AuthJsonSerializer {
	static Map<String, Object> getStringObjectMap(String bootstrapServer, String apiKey, String apiSecret) {
		Map<String, Object> props = new HashMap<>();

		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		props.put(JsonSerializer.TYPE_MAPPINGS, "JoinEventDto:spharos.nu.auth.domain.auth.dto.event.JoinEventDto");

		// 롤백 관련 설정
		props.put(ProducerConfig.RETRIES_CONFIG, 3); // 재시도 횟수
		props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 5000); // 재시도 간 대기 시간(ms)
		props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1); // 동시 처리 요청 수
		props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 30000); // 메시지 전송 시간 초과(ms)

		/*confluent kafka 연결을 위한 설정*/
		props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,"SASL_SSL");
		props.put(SaslConfigs.SASL_MECHANISM,"PLAIN");
		props.put(SaslConfigs.SASL_JAAS_CONFIG,String.format("org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";", apiKey, apiSecret));

		return props;
	}
}
