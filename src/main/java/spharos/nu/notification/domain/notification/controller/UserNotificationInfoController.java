package spharos.nu.notification.domain.notification.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spharos.nu.notification.domain.notification.dto.request.NotificationTokenDto;
import spharos.nu.notification.domain.notification.service.UserNotificationInfoService;
import spharos.nu.notification.global.apiresponse.ApiResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "notification", description = "알림관련 API")
@RequestMapping("/api/v1/notification")
public class UserNotificationInfoController {

    private final UserNotificationInfoService userNotificationInfoService;

    @PostMapping("/token")
    @Operation(summary = "디바이스 토큰 저장", description = "디바이스 토큰 저장")
    public ResponseEntity<ApiResponse<Void>> saveToken(
            @RequestHeader("User-Uuid") String uuid,
            @RequestBody NotificationTokenDto notificationTokenDto) {
        userNotificationInfoService.saveToken(uuid, notificationTokenDto);
        return ApiResponse.success(null, "디바이스 토큰 저장 성공");
    }


    @PutMapping("/setting")
    @Operation(summary = "알림 설정 변경", description = "알림 설정 변경")
    public ResponseEntity<ApiResponse<Void>> updateSetting(
            @RequestHeader("User-Uuid") String uuid,
            @RequestParam("isNotify") boolean isNotify) {
        userNotificationInfoService.changeNotify(uuid, isNotify);
        return ApiResponse.success(null, "알림 설정 변경 성공");
    }


}
