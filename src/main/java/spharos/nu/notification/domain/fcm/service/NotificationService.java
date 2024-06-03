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
import spharos.nu.notification.domain.fcm.repository.NotificationRepository;
import spharos.nu.notification.domain.fcm.dto.FcmMessageDto;
import spharos.nu.notification.domain.fcm.dto.NotificationSendDto;
import spharos.nu.notification.global.exception.CustomException;

import static spharos.nu.notification.global.exception.errorcode.ErrorCode.FCM_SEND_FAIL;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    @Value("${fcm.certification}")
    String firebaseConfigPath;

    private final NotificationRepository fcmRepository;

    @Transactional
    public void sendMessageTo(NotificationSendDto notificationSendDto) throws IOException {

        String message = makeMessage(notificationSendDto);
        log.info("message : " + message);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(message, headers);

        String API_URL = "https://fcm.googleapis.com/v1/projects/goodsgoodsduck/messages:send";

        try {
            restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
        } catch (Exception e){
            throw new CustomException(FCM_SEND_FAIL);
        }

        saveMessage(notificationSendDto);

    }

    private String getAccessToken() throws IOException {
        final GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }


    private String makeMessage(NotificationSendDto notificationSendDto) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        FcmMessageDto fcmMessageDto = FcmMessageDto.builder()
                .message(FcmMessageDto.Message.builder()
                        .token(notificationSendDto.getToken())
                        .notification(FcmMessageDto.Notification.builder()
                                .title(notificationSendDto.getTitle())
                                .body(notificationSendDto.getBody())
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessageDto);
    }


    @Transactional
    private void saveMessage(NotificationSendDto notificationSendDto) {
        fcmRepository.save(spharos.nu.notification.domain.fcm.entity.Notification.builder()
                .title(notificationSendDto.getTitle())
                .body(notificationSendDto.getBody())
                .userUuid(notificationSendDto.getUserUuid())
                .isRead(false)
                .type((byte) 1)
                .build()
        );

    }
}