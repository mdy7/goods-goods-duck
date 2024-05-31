package spharos.nu.notification.domain.fcm.entity;

import jakarta.persistence.*;
import lombok.*;
import spharos.nu.notification.global.entity.CreatedAtBaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notification extends CreatedAtBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    private String title;
    private String body;
    private String userUuid;
    private boolean isRead;
    private byte type;



}
