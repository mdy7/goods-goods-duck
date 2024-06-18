package spharos.nu.goods.domain.goods.dto.event;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder
public class NotificationEventDto {
    private String title;
    private String content;
    private List<String> uuid;
    private String link;
}