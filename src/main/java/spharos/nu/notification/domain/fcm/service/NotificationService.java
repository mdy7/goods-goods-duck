package spharos.nu.notification.domain.fcm.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spharos.nu.notification.domain.fcm.dto.request.FcmSendDto;
import spharos.nu.notification.domain.fcm.dto.request.NotificationSaveDto;
import spharos.nu.notification.domain.fcm.dto.response.NotificationListDto;
import spharos.nu.notification.domain.fcm.dto.response.NotificationInfoDto;
import spharos.nu.notification.domain.fcm.entity.Notification;
import spharos.nu.notification.domain.fcm.entity.UserNotificationInfo;
import spharos.nu.notification.domain.fcm.repository.NotificationRepository;
import spharos.nu.notification.domain.fcm.repository.UserNotificationInfoRepository;
import spharos.nu.notification.global.exception.CustomException;


import static spharos.nu.notification.global.exception.errorcode.ErrorCode.NOT_FOUND_NOTIFICATION;
import static spharos.nu.notification.global.exception.errorcode.ErrorCode.NOT_FOUND_USER_NOTIFICATION_INFO;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FcmService fcmService;
    private final UserNotificationInfoRepository userNotificationInfoRepository;
    /**
     * 알림 저장 후 푸시알림 보냄
     */
    @Transactional
    public void addNotification(NotificationSaveDto notificationSaveDto) {
        Notification notification = Notification.builder()
                .title(notificationSaveDto.getTitle())
                .content(notificationSaveDto.getContent())
                .uuid(notificationSaveDto.getUuid())
                .isRead(false)
                .notificationType(notificationSaveDto.getNotificationType())
                .build();

        notificationRepository.save(notification);

        // 푸시알림 전송
        try {
            sendPushAlarm(notificationSaveDto);
        } catch (Exception e) {
            log.error("푸시알림 전송 실패 : {}", e.getMessage());
        }

    }

    private void sendPushAlarm(NotificationSaveDto notificationSaveDto) {
        UserNotificationInfo userNotificationInfo = userNotificationInfoRepository.findByUuid(notificationSaveDto.getUuid())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER_NOTIFICATION_INFO));

        if (userNotificationInfo.isNotify()) {
            FcmSendDto fcmSendDto = FcmSendDto.builder()
                    .token(userNotificationInfo.getDeviceToken())
                    .title(notificationSaveDto.getTitle())
                    .content(notificationSaveDto.getContent())
                    .build();
            fcmService.sendMessageTo(fcmSendDto);
        }
    }

    /**
     * 알림 삭제
     */
    @Transactional
    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(()
                -> new CustomException(NOT_FOUND_NOTIFICATION));

        notificationRepository.delete(notification);
    }

    /**
     * 알림 전체 조회
     */
    public NotificationListDto findAllNotification(String uuid, Pageable pageable) {
        Page<NotificationInfoDto> notificationPage = notificationRepository.findByUuid(uuid, pageable);

        return NotificationListDto.builder()
                .maxPage(notificationPage.getTotalPages())
                .nowPage(notificationPage.getNumber())
                .totalCount(notificationPage.getTotalElements())
                .isLast(notificationPage.isLast())
                .notificationList(notificationPage.getContent())
                .build();
    }

    /**
     * 알림 읽음 처리
     */
    @Transactional
    public void readNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(()
                -> new CustomException(NOT_FOUND_NOTIFICATION));

        Notification updateNotification = Notification.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .uuid(notification.getUuid())
                .isRead(true)
                .notificationType(notification.getNotificationType())
                .build();

        notificationRepository.save(updateNotification);
    }


}
