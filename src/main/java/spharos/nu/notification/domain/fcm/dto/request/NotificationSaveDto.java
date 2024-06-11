package spharos.nu.notification.domain.fcm.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotificationSaveDto {

    private String title;
    private String content;
    private String uuid;
    private byte notificationType;
}
