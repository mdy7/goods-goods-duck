package spharos.nu.notification.domain.fcm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spharos.nu.notification.domain.fcm.dto.request.NotificationSaveDto;
import spharos.nu.notification.domain.fcm.service.FcmService;
import spharos.nu.notification.domain.fcm.service.NotificationService;
import spharos.nu.notification.global.apiresponse.ApiResponse;

import java.io.IOException;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification-n")
public class NotificationNController {

    private final NotificationService notificationService;
    private final FcmService fcmService;

    @PostMapping("/fcm/send")
    public ResponseEntity<ApiResponse<Void>> sendFcm(@RequestBody NotificationSaveDto notificationSaveDto) {
        notificationService.addNotification(notificationSaveDto);
        return ApiResponse.success(null, "알림 저장 완료");
    }



}