package spharos.nu.aggregation.domain.aggregation.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class CommonJsonDeserializer {

	static Map<String, Object> getStringObjectMap(String bootstrapServer, String apiKey, String apiSecret) {
		Map<String, Object> props = new HashMap<>();

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "aggregation-group");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
		props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
		props.put(JsonDeserializer.TRUSTED_PACKAGES,"*");
		props.put(org.springframework.kafka.support.serializer.JsonDeserializer.TYPE_MAPPINGS,
			"GoodsCreateEventDto:spharos.nu.aggregation.domain.aggregation.dto.event.GoodsCreateEventDto," +
			"GoodsDeleteEventDto:spharos.nu.aggregation.domain.aggregation.dto.event.GoodsDeleteEventDto"
			);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");  //메시지 오프셋 설정
		props.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, 50); // 재연결 시도 간격 50밀리초
		props.put(ConsumerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, 60000); // 최대 재연결 시도 간격 1분
		/*confluent kafka 연결을 위한 설정*/
		props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,"SASL_SSL");
		props.put(SaslConfigs.SASL_MECHANISM,"PLAIN");
		props.put(SaslConfigs.SASL_JAAS_CONFIG,String.format("org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";", apiKey, apiSecret));

		return props;
	}
}

