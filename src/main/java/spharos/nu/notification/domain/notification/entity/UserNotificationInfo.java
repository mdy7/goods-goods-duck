package spharos.nu.notification.domain.notification.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserNotificationInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_notification_info_id")
    private Long id;

    private String deviceToken;
    private boolean isNotify;
    private String uuid;



}
