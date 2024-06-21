package spharos.nu.notification.domain.notification.dto.request;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class NotificationRemoveDto {

    private List<String> notificationId;
}
