package spharos.nu.aggregation.domain.aggregation.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class CommonJsonSerializer {

	static Map<String, Object> getStringObjectMap(String bootstrapServer, String apiKey, String apiSecret) {
		Map<String, Object> props = new HashMap<>();

		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		props.put(JsonSerializer.TYPE_MAPPINGS,
			"CountEventDto:spharos.nu.aggregation.domain.aggregation.dto.event.CountEventDto"
		);
		/*confluent kafka 연결을 위한 설정*/
		props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,"SASL_SSL");
		props.put(SaslConfigs.SASL_MECHANISM,"PLAIN");
		props.put(SaslConfigs.SASL_JAAS_CONFIG,String.format("org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";", apiKey, apiSecret));

		return props;
	}
}
