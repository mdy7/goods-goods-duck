package spharos.nu.member.global.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import spharos.nu.member.domain.member.dto.request.KafkaUserCreatedDto;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
	@Value("${spring.kafka.bootstrap-server}")
	private String bootstrapServer;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	@Bean
	public ConsumerFactory<Long, KafkaUserCreatedDto> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "spharos.nu.auth.domain.auth.dto");

		return new DefaultKafkaConsumerFactory<>(props, new LongDeserializer(), new JsonDeserializer<>(KafkaUserCreatedDto.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<Long, KafkaUserCreatedDto> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<Long, KafkaUserCreatedDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}
}
