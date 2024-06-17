package spharos.nu.goods.domain.goods.kafka.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import lombok.Getter;
import spharos.nu.goods.domain.goods.dto.event.CloseEventDto;
import spharos.nu.goods.domain.goods.dto.event.GoodsCreateEventDto;
import spharos.nu.goods.domain.goods.dto.event.GoodsDeleteEventDto;
import spharos.nu.goods.domain.goods.dto.event.GoodsDisableEventDto;
import spharos.nu.goods.domain.goods.dto.event.GoodsStatusEventDto;
import spharos.nu.goods.domain.goods.dto.event.OpenEventDto;

@Configuration
public class GoodsProducerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;

	@Value("${spring.kafka.api-key}")
	private String apiKey;

	@Value("${spring.kafka.api-secret}")
	private String apiSecret;

	@Bean
	public Map<String, Object> goodsProducerConfigs() {
		return CommonJsonSerializer.getStringObjectMap(bootstrapServer,apiKey,apiSecret);
	}
    /* 경매 시작 카프카템플릿*/
	@Bean
	public ProducerFactory<String, OpenEventDto> openEventDtoProducerFactory() {
		return new DefaultKafkaProducerFactory<>(goodsProducerConfigs());
	}
	@Bean
	public KafkaTemplate<String, OpenEventDto> openEventDtoKafkaTemplate() {
		return new KafkaTemplate<>(openEventDtoProducerFactory());
	}

    /* 경매 마감 카프카템플릿*/
	@Bean
	public ProducerFactory<String, CloseEventDto> closeEventDtoProducerFactory() {
		return new DefaultKafkaProducerFactory<>(goodsProducerConfigs());
	}
	@Bean
	public KafkaTemplate<String, CloseEventDto> closeEventDtoKafkaTemplate() {
		return new KafkaTemplate<>(closeEventDtoProducerFactory());
	}

	/* 굿즈 생성 카프카템플릿*/
	@Bean
	public ProducerFactory<String, GoodsCreateEventDto> goodsCreateEventDtoProducerFactory() {
		return new DefaultKafkaProducerFactory<>(goodsProducerConfigs());
	}
	@Bean
	public KafkaTemplate<String, GoodsCreateEventDto> goodsCreateDtoKafkaTemplate() {
		return new KafkaTemplate<>(goodsCreateEventDtoProducerFactory());
	}

	/* 굿즈 삭제 카프카템플릿*/
	@Bean
	public ProducerFactory<String, GoodsDeleteEventDto> goodsDeleteEventDtoProducerFactory() {
		return new DefaultKafkaProducerFactory<>(goodsProducerConfigs());
	}
	@Bean
	public KafkaTemplate<String, GoodsDeleteEventDto> goodsDeleteDtoKafkaTemplate() {
		return new KafkaTemplate<>(goodsDeleteEventDtoProducerFactory());
	}

	/* 굿즈 상태변경 카프카템플릿*/
	@Bean
	public ProducerFactory<String, GoodsStatusEventDto> goodsStatusEventDtoProducerFactory() {
		return new DefaultKafkaProducerFactory<>(goodsProducerConfigs());
	}
	@Bean
	public KafkaTemplate<String, GoodsStatusEventDto> goodsStatusDtoKafkaTemplate() {
		return new KafkaTemplate<>(goodsStatusEventDtoProducerFactory());
	}

	/* 굿즈 숨김변경 카프카템플릿*/
	@Bean
	public ProducerFactory<String, GoodsDisableEventDto> goodsDisableEventDtoProducerFactory() {
		return new DefaultKafkaProducerFactory<>(goodsProducerConfigs());
	}
	@Bean
	public KafkaTemplate<String, GoodsDisableEventDto> goodsDisableDtoKafkaTemplate() {
		return new KafkaTemplate<>(goodsDisableEventDtoProducerFactory());
	}

}
