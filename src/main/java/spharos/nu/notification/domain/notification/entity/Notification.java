package spharos.nu.notification.domain.notification.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notification")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notification {

    @Id
    private String id;
    private String title;
    private String content;
    private String uuid;
    private String link;
    private boolean isRead;
    private byte notificationType;

    @CreatedDate
    private LocalDateTime createdAt;
}
