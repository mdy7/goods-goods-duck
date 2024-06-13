package spharos.nu.goods.domain.goods.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class CommonJsonDeserializer {

	static Map<String, Object> getStringObjectMap(String bootstrapServer) {
		Map<String, Object> props = new HashMap<>();

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "goods-group");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
		props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		props.put(org.springframework.kafka.support.serializer.JsonDeserializer.TYPE_MAPPINGS,
			"spharos.nu.goods.domain.goods.dto.event.TradingCompleteEventDto");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");  //메시지 오프셋 설정
		props.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, 5000); // 재연결 시도 간격 5초
		props.put(ConsumerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, 60000); // 최대 재연결 시도 간격 1분
		return props;
	}
}
