package spharos.nu.notification.domain.notification.kafka.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import spharos.nu.notification.domain.notification.kafka.dto.NotificationEventDto;
import spharos.nu.notification.domain.notification.service.NotificationService;

import java.io.IOException;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaConsumer {

    private final NotificationService notificationService;


    @KafkaListener(topics = "notification-topic", containerFactory = "notificationListenerContainerEventFactory")
    public void notificationEvent(NotificationEventDto notificationEventDto) {
        log.info("Notification Event : {}", notificationEventDto);
        notificationService.addNotification(notificationEventDto);
    }

}