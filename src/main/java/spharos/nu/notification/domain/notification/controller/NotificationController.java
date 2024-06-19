package spharos.nu.notification.domain.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spharos.nu.notification.domain.notification.dto.request.NotificationReadDto;
import spharos.nu.notification.domain.notification.dto.request.NotificationRemoveDto;
import spharos.nu.notification.domain.notification.dto.request.NotificationSaveDto;
import spharos.nu.notification.domain.notification.dto.response.NotificationListDto;
import spharos.nu.notification.domain.notification.kafka.dto.NotificationEventDto;
import spharos.nu.notification.domain.notification.service.NotificationService;
import spharos.nu.notification.global.apiresponse.ApiResponse;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "notification", description = "알림관련 API")
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "알림 조회", description = "알림 조회")
    public ResponseEntity<ApiResponse<NotificationListDto>> notificationList(
            @RequestHeader("User-Uuid") String uuid,
            @PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponse.success(notificationService.findAllNotification(uuid, pageable), "알림 조회 성공");
    }

    @PostMapping
    @Operation(summary = "알림 저장", description = "알림 저장")
    public ResponseEntity<ApiResponse<Void>> notificationSave(
            @RequestBody NotificationEventDto notificationEventDto
    ) {
        notificationService.addNotification(notificationEventDto);
        return ApiResponse.success(null, "알림 저장 성공");
    }

    @PutMapping("/read")
    @Operation(summary = "알림 읽음 처리", description = "알림 읽음 처리")
    public ResponseEntity<ApiResponse<Void>> notificationRead(
            @RequestBody NotificationReadDto notificationReadDto
            ) {
        notificationService.readNotification(notificationReadDto);
        return ApiResponse.success(null, "알림 읽음 처리 성공");
    }

    @DeleteMapping
    @Operation(summary = "알림 삭제", description = "알림 삭제")
    public ResponseEntity<ApiResponse<Void>> notificationDelete(
            @RequestBody NotificationRemoveDto notificationRemoveDto
            ) {
        notificationService.deleteNotification(notificationRemoveDto);
        return ApiResponse.success(null, "알림 삭제 성공");
    }


}