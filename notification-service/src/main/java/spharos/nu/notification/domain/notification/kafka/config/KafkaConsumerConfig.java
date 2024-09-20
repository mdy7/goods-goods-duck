package spharos.nu.notification.domain.notification.kafka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import spharos.nu.notification.domain.notification.kafka.dto.NotificationEventDto;

import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Value("${spring.kafka.api-key}")
    private String apiKey;

    @Value("${spring.kafka.api-secret}")
    private String apiSecret;

    @Bean
    public Map<String,Object> notificationConsumerConfigs() {
        return CommonJsonDeserializer.getStringObjectMap(bootstrapServer,apiKey,apiSecret);
    }

    @Bean
    public ConsumerFactory<String, NotificationEventDto> notificationConsumerFactory(){
        return new DefaultKafkaConsumerFactory<>(notificationConsumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NotificationEventDto> notificationListenerContainerEventFactory() {
        ConcurrentKafkaListenerContainerFactory<String,NotificationEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(notificationConsumerFactory());
        return factory;
    }

    @Bean
    public StringJsonMessageConverter jsonMessageConverter() {
        return new StringJsonMessageConverter();
    }

}
