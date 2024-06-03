package spharos.nu.notification.domain.fcm.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationSendDto {
    private String token;

    private String title;

    private String body;

    private String userUuid;

    @Builder(toBuilder = true)
    public NotificationSendDto(String token, String title, String body) {
        this.token = token;
        this.title = title;
        this.body = body;
    }
}