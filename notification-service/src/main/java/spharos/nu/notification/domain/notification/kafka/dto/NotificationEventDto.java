package spharos.nu.notification.domain.notification.kafka.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class NotificationEventDto {
    private String title;
    private String content;
    private List<String> uuid;
    private String link;
}
