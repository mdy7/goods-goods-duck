package spharos.nu.notification.domain.fcm.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import spharos.nu.notification.domain.fcm.dto.request.FcmSendDto;
import spharos.nu.notification.domain.fcm.dto.request.FcmMessageDto;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FcmService {

    @Value("${fcm.certification}")
    String firebaseConfigPath;

    @Transactional
    public void sendMessageTo(FcmSendDto fcmSendDto) {
        try {
            String message = makeMessage(fcmSendDto);
            log.info("message : " + message);
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + getAccessToken());

            HttpEntity<String> entity = new HttpEntity<>(message, headers);

            String API_URL = "https://fcm.googleapis.com/v1/projects/goodsgoodsduck/messages:send";

            restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
            log.info("푸시알림 전송 성공");
        } catch (Exception e) {
            log.error("푸시알림 전송 실패 : {}", e.getMessage());
//            throw new CustomException(FCM_SEND_FAIL);
        }

    }

    private String getAccessToken() throws IOException {
        final GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }


    private String makeMessage(FcmSendDto fcmSendDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        FcmMessageDto fcmMessageDto = FcmMessageDto.builder()
                .message(FcmMessageDto.Message.builder()
                        .token(fcmSendDto.getToken())
                        .notification(FcmMessageDto.Notification.builder()
                                .title(fcmSendDto.getTitle())
                                .body(fcmSendDto.getContent())
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessageDto);
    }
}