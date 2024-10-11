package spharos.nu.goods.domain.goods.kafka.config;

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
		props.put(org.springframework.kafka.support.serializer.JsonSerializer.TYPE_MAPPINGS,
			"GoodsCreateEventDto:spharos.nu.goods.domain.goods.dto.event.GoodsCreateEventDto," +
			"GoodsDeleteEventDto:spharos.nu.goods.domain.goods.dto.event.GoodsDeleteEventDto," +
			"GoodsDisableEventDto:spharos.nu.goods.domain.goods.dto.event.GoodsDisableEventDto," +
			"GoodsStatusEventDto:spharos.nu.goods.domain.goods.dto.event.GoodsStatusEventDto," +
			"NotificationEventDto:spharos.nu.goods.domain.goods.dto.event.NotificationEventDto"
		);


//		props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,"AD");


		return props;
	}
}
