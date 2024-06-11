package spharos.nu.notification.domain.notification.entity;

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
    private Long id;

    private String title;
    private String content;
    private String uuid;
    private boolean isRead;
    private byte notificationType;



}
