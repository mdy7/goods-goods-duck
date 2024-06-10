package spharos.nu.goods.domain.goods.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class CommonJsonSerializer {

	static Map<String, Object> getStringObjectMap(String bootstrapServer) {
		Map<String, Object> props = new HashMap<>();

		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		props.put(org.springframework.kafka.support.serializer.JsonSerializer.TYPE_MAPPINGS,
		    "CloseEventDto:spharos.nu.goods.domain.goods.dto.CloseEventDto," +
			"OpenEventDto:spharos.nu.goods.domain.goods.dto.OpenEventDto");

		return props;
	}
}
