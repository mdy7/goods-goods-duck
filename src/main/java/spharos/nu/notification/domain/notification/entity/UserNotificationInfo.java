package spharos.nu.notification.domain.notification.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_notification_info")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserNotificationInfo {

    @Id
    private String id;
    private String deviceToken;
    private boolean isNotify;
    private String uuid;



}
