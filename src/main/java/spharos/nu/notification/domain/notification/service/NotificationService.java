package spharos.nu.notification.domain.notification.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spharos.nu.notification.domain.notification.dto.request.FcmSendDto;
import spharos.nu.notification.domain.notification.dto.request.NotificationReadDto;
import spharos.nu.notification.domain.notification.dto.request.NotificationRemoveDto;
import spharos.nu.notification.domain.notification.dto.response.NotificationInfoDto;
import spharos.nu.notification.domain.notification.dto.response.NotificationListDto;
import spharos.nu.notification.domain.notification.entity.Notification;
import spharos.nu.notification.domain.notification.entity.UserNotificationInfo;
import spharos.nu.notification.domain.notification.kafka.dto.NotificationEventDto;
import spharos.nu.notification.domain.notification.repository.NotificationRepository;
import spharos.nu.notification.domain.notification.repository.UserNotificationInfoRepository;
import spharos.nu.notification.global.exception.CustomException;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
     * 알림 저장
     */
    @Transactional
    public void addNotification(NotificationEventDto notificationEventDto) {
        List<Notification> notifications = new ArrayList<>();
        for (String uuid : notificationEventDto.getUuid()) {
            Notification notification = Notification.builder()
                    .title(notificationEventDto.getTitle())
                    .content(notificationEventDto.getContent())
                    .link(notificationEventDto.getLink())
                    .uuid(uuid)
                    .build();
            notifications.add(notification);
        }
        notificationRepository.saveAll(notifications);

        sendPushAlarm(notificationEventDto);
    }

    /**
     * 푸시 알림 전송
     */
    public void sendPushAlarm(NotificationEventDto notificationEventDto) {
        for (String uuid : notificationEventDto.getUuid()) {
            UserNotificationInfo userNotificationInfo = userNotificationInfoRepository.findByUuid(uuid).orElseThrow(()
                    -> new CustomException(NOT_FOUND_USER_NOTIFICATION_INFO));

            FcmSendDto fcmSendDto = FcmSendDto.builder()
                    .tokens(Collections.singletonList(userNotificationInfo.getDeviceToken()))
                    .title(notificationEventDto.getTitle())
                    .content(notificationEventDto.getContent())
                    .link(notificationEventDto.getLink())
                    .build();

            fcmService.sendMessageTo(fcmSendDto);
        }
    }

    /**
     * 알림 삭제
     */
    @Transactional
    public void deleteNotification(NotificationRemoveDto notificationRemoveDto) {
        for (String notificationId : notificationRemoveDto.getNotificationId()) {
            notificationRepository.deleteById(notificationId);
        }
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
    public void readNotification(NotificationReadDto notificationReadDto) {
        for (String notificationId : notificationReadDto.getNotificationId()) {
            Notification notification = notificationRepository.findById(notificationId).orElseThrow(()
                    -> new CustomException(NOT_FOUND_NOTIFICATION));

            notificationRepository.save(
                    Notification.builder()
                            .id(notification.getId())
                            .title(notification.getTitle())
                            .content(notification.getContent())
                            .link(notification.getLink())
                            .uuid(notification.getUuid())
                            .isRead(true)
                            .build()
            );
        }
    }


}
