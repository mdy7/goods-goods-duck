package spharos.nu.read.domain.goods.kafka.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import lombok.RequiredArgsConstructor;
import spharos.nu.read.domain.goods.dto.event.CountEventDto;
import spharos.nu.read.domain.goods.dto.event.GoodsCreateEventDto;
import spharos.nu.read.domain.goods.dto.event.GoodsDeleteEventDto;
import spharos.nu.read.domain.goods.dto.event.GoodsDisableEventDto;
import spharos.nu.read.domain.goods.dto.event.GoodsStatusEventDto;

@Configuration
@RequiredArgsConstructor
public class ReadConsumerConfig {
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;

	@Value("${spring.kafka.api-key}")
	private String apiKey;

	@Value("${spring.kafka.api-secret}")
	private String apiSecret;

	@Bean
	public Map<String,Object> readConsumerConfigs() {
		return CommonJsonDeserializer.getStringObjectMap(bootstrapServer,apiKey,apiSecret);
	}

	/* 집계관련 리스너*/
	@Bean
	public ConsumerFactory<String, CountEventDto> countEventDtoConsumerFactory(){
		return new DefaultKafkaConsumerFactory<>(readConsumerConfigs());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, CountEventDto> countEventListener() {
		ConcurrentKafkaListenerContainerFactory<String,CountEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(countEventDtoConsumerFactory());
		return factory;
	}

	/* 굿즈생성이벤트 리스너*/
	@Bean
	public ConsumerFactory<String, GoodsCreateEventDto> goodsCreateEventDtoConsumerFactory(){
		return new DefaultKafkaConsumerFactory<>(readConsumerConfigs());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, GoodsCreateEventDto> goodsCreateEventListener() {
		ConcurrentKafkaListenerContainerFactory<String,GoodsCreateEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(goodsCreateEventDtoConsumerFactory());
		return factory;
	}

	/* 굿즈삭제이벤트 리스너*/
	@Bean
	public ConsumerFactory<String, GoodsDeleteEventDto> goodsDeleteEventDtoConsumerFactory(){
		return new DefaultKafkaConsumerFactory<>(readConsumerConfigs());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, GoodsDeleteEventDto> goodsDeleteEventListener() {
		ConcurrentKafkaListenerContainerFactory<String,GoodsDeleteEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(goodsDeleteEventDtoConsumerFactory());
		return factory;
	}

	/* 굿즈상태(변경)이벤트 리스너*/
	@Bean
	public ConsumerFactory<String, GoodsStatusEventDto> goodsStatusEventDtoConsumerFactory(){
		return new DefaultKafkaConsumerFactory<>(readConsumerConfigs());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, GoodsStatusEventDto> goodsStatusEventListener() {
		ConcurrentKafkaListenerContainerFactory<String,GoodsStatusEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(goodsStatusEventDtoConsumerFactory());
		return factory;
	}

	/* 굿즈숨김(변경)이벤트 리스너*/
	@Bean
	public ConsumerFactory<String, GoodsDisableEventDto> goodsDisableEventDtoConsumerFactory(){
		return new DefaultKafkaConsumerFactory<>(readConsumerConfigs());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, GoodsDisableEventDto> goodsDisableEventListener() {
		ConcurrentKafkaListenerContainerFactory<String,GoodsDisableEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(goodsDisableEventDtoConsumerFactory());
		return factory;
	}

	@Bean
	public StringJsonMessageConverter jsonMessageConverter() {
		return new StringJsonMessageConverter();
	}
}
