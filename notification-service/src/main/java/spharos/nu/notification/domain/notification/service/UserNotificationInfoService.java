package spharos.nu.notification.domain.notification.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spharos.nu.notification.domain.notification.dto.request.NotificationTokenDto;
import spharos.nu.notification.domain.notification.entity.UserNotificationInfo;
import spharos.nu.notification.domain.notification.repository.UserNotificationInfoRepository;
import spharos.nu.notification.global.exception.CustomException;

import java.util.Optional;

import static spharos.nu.notification.global.exception.errorcode.ErrorCode.NOT_FOUND_USER_NOTIFICATION_INFO;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserNotificationInfoService {

    private final UserNotificationInfoRepository userNotificationInfoRepository;

    @Transactional
    public void saveToken(NotificationTokenDto notificationTokenDto) {
        Optional<UserNotificationInfo> optionalUserNotificationInfo = userNotificationInfoRepository.findByUuid(notificationTokenDto.getUuid());

        if (optionalUserNotificationInfo.isPresent()) {
            // 유저 정보가 있으면 토큰 업데이트
            UserNotificationInfo userNotificationInfo = optionalUserNotificationInfo.get();
            UserNotificationInfo updateUserNotificationInfo = UserNotificationInfo.builder()
                    .id(userNotificationInfo.getId())
                    .uuid(userNotificationInfo.getUuid())
                    .deviceToken(notificationTokenDto.getToken())
                    .isNotify(userNotificationInfo.isNotify())
                    .build();

            userNotificationInfoRepository.save(updateUserNotificationInfo);
        } else {
            // 유저 정보가 없으면 신규 저장
            UserNotificationInfo newUserNotificationInfo = UserNotificationInfo.builder()
                    .uuid(notificationTokenDto.getUuid())
                    .deviceToken(notificationTokenDto.getToken())
                    .isNotify(true)
                    .build();
            userNotificationInfoRepository.save(newUserNotificationInfo);
        }

    }

    @Transactional
    public void changeNotify(String uuid, boolean isNotify) {
        UserNotificationInfo userNotificationInfo = userNotificationInfoRepository.findByUuid(uuid).orElseThrow(() ->
                new CustomException(NOT_FOUND_USER_NOTIFICATION_INFO));

        UserNotificationInfo updateUserNotificationInfo = UserNotificationInfo.builder()
                .id(userNotificationInfo.getId())
                .uuid(userNotificationInfo.getUuid())
                .deviceToken(userNotificationInfo.getDeviceToken())
                .isNotify(isNotify)
                .build();

        userNotificationInfoRepository.save(updateUserNotificationInfo);


    }
}
