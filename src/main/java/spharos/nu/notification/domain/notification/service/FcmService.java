package spharos.nu.notification.domain.notification.service;



import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import spharos.nu.notification.domain.notification.dto.request.FcmSendDto;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FcmService {

    @Value("${fcm.certification}")
    String firebaseConfigPath;

    private final FirebaseMessaging firebaseMessaging;

    public void sendMessageTo(FcmSendDto fcmSendDto) {
        try {
            Notification notification = Notification.builder()
                    .setTitle(fcmSendDto.getTitle())
                    .setBody(fcmSendDto.getContent())
                    .build();

            MulticastMessage message = MulticastMessage.builder()
                    .addAllTokens(fcmSendDto.getTokens()) // 여기서 여러 개의 토큰을 추가
                    .setNotification(notification)
                    .build();

            BatchResponse response = firebaseMessaging.sendMulticast(message);

            // 전송 실패한 토큰 확인
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                List<String> failedTokens = new ArrayList<>();
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        failedTokens.add(fcmSendDto.getTokens().get(i));
                    }
                }
                log.error("푸시알림 전송 실패 수: {}", responses.size());
                log.error("실패한 토큰: {}", failedTokens);
            } else {
                log.info("푸시알림 전송 성공");
            }
        } catch (Exception e) {
            log.error("푸시알림 전송 실패 : {}", e.getMessage());
        }
    }


}