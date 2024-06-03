package spharos.nu.notification.domain.fcm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spharos.nu.notification.domain.fcm.service.NotificationService;
import spharos.nu.notification.domain.fcm.dto.NotificationSendDto;
import spharos.nu.notification.global.apiresponse.ApiResponse;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification-n")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Void>> pushMessage(@RequestBody @Validated NotificationSendDto notificationSendDto) throws IOException {
        notificationService.sendMessageTo(notificationSendDto);
        return ApiResponse.success(null, "알림 전송 성공");
    }

}