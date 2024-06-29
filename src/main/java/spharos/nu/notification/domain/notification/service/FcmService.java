package spharos.nu.notification.domain.notification.service;



import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import spharos.nu.notification.domain.notification.dto.request.FcmSendDto;




@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FcmService {

    @Value("${fcm.front-url}")
    String FRONT_URL;

    private final FirebaseMessaging firebaseMessaging;

    public void sendMessageTo(FcmSendDto fcmSendDto) {
        Notification notification = Notification.builder()
                .setTitle(fcmSendDto.getTitle())
                .setBody(fcmSendDto.getContent())
                .build();

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(fcmSendDto.getTokens())
                .setNotification(notification)
                .putData("title", fcmSendDto.getTitle())
                .putData("body", fcmSendDto.getContent())
                .putData("link", FRONT_URL + fcmSendDto.getLink())
                .build();

        try {
            firebaseMessaging.sendMulticast(message);
        } catch (Exception e) {
            log.error("Failed to send message: " + e.getMessage());
        }
    }


}