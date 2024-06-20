package spharos.nu.goods.domain.goods.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.goods.domain.goods.dto.event.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoodsKafkaProducer {

	private final KafkaTemplate<String, GoodsCreateEventDto> goodsCreateKafkaTemplate;
	private final KafkaTemplate<String, GoodsDeleteEventDto> goodsDeleteKafkaTemplate;
	private final KafkaTemplate<String, GoodsStatusEventDto> goodsStatusKafkaTemplate;
	private final KafkaTemplate<String, GoodsDisableEventDto> goodsDisableKafkaTemplate;
	private final KafkaTemplate<String, NotificationEventDto> goodsNotificationKafkaTemplate;


	public void sendGoodsCreateEvent(GoodsCreateEventDto goodsCreateEventDto) {
		goodsCreateKafkaTemplate.send("goods-create-topic",goodsCreateEventDto);
	};

	public void sendGoodsDeleteEvent(GoodsDeleteEventDto goodsDeleteEventDto) {
		goodsDeleteKafkaTemplate.send("goods-delete-topic",goodsDeleteEventDto);
	};

	public void sendGoodsStatusEvent(GoodsStatusEventDto goodsStatusEventDto) {
		goodsStatusKafkaTemplate.send("goods-status-topic",goodsStatusEventDto);
	};

	public void sendGoodsDisableEvent(GoodsDisableEventDto goodsDisableEventDto) {
		goodsDisableKafkaTemplate.send("goods-disable-topic",goodsDisableEventDto);
	};

	public void sendNotificationEvent(NotificationEventDto notificationEventDto) {
		goodsNotificationKafkaTemplate.send("notification-topic", notificationEventDto);
	}
}
