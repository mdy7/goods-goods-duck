package spharos.nu.notification.domain.notification.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spharos.nu.notification.domain.notification.dto.request.NotificationTokenDto;
import spharos.nu.notification.domain.notification.entity.UserNotificationInfo.DeviceToken;
import spharos.nu.notification.domain.notification.entity.UserNotificationInfo;
import spharos.nu.notification.domain.notification.repository.UserNotificationInfoRepository;
import spharos.nu.notification.global.exception.CustomException;

import java.util.ArrayList;
import java.util.List;
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

        UserNotificationInfo userNotificationInfo = userNotificationInfoRepository.findByUuid(notificationTokenDto.getUuid()).orElseGet(
                () -> UserNotificationInfo.builder()
                        .uuid(notificationTokenDto.getUuid())
                        .deviceToken(new ArrayList<>())
                        .build()
        );

        // 중복 토큰 확인
        List<DeviceToken> deviceTokens = userNotificationInfo.getDeviceToken();
        boolean tokenExists = deviceTokens.stream()
                .anyMatch(token -> token.getToken().equals(notificationTokenDto.getToken()));

        if (!tokenExists) {
            deviceTokens.add(DeviceToken.builder()
                    .token(notificationTokenDto.getToken())
                    .build());
            userNotificationInfoRepository.save(userNotificationInfo);
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
