package spharos.nu.notification.domain.fcm.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spharos.nu.notification.domain.fcm.dto.response.NotificationListDto;
import spharos.nu.notification.domain.fcm.service.NotificationService;
import spharos.nu.notification.global.apiresponse.ApiResponse;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "notification", description = "알림관련 API")
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Tag(name = "notification", description = "알림 전체 조회")
    public ResponseEntity<ApiResponse<NotificationListDto>> notificationList(
            @RequestHeader("User-Uuid") String uuid,
            @PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponse.success(notificationService.findAllNotification(uuid, pageable), "알림 조회 성공");
    }

    @PutMapping("/{notificationId}/read")
    @Tag(name = "notification", description = "알림 읽음 처리")
    public ResponseEntity<ApiResponse<Void>> notificationRead(
            @PathVariable("notificationId") Long notificationId
    ) {
        notificationService.readNotification(notificationId);
        return ApiResponse.success(null, "알림 읽음 처리 성공");
    }

    @DeleteMapping("/{notificationId}")
    @Tag(name = "notification", description = "알림 삭제")
    public ResponseEntity<ApiResponse<Void>> notificationDelete(
            @PathVariable("notificationId") Long notificationId
    ) {
        notificationService.deleteNotification(notificationId);
        return ApiResponse.success(null, "알림 삭제 성공");
    }


}