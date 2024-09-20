package spharos.nu.notification.domain.notification.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NotificationTokenDto {
    private String uuid;
    private String token;
}
