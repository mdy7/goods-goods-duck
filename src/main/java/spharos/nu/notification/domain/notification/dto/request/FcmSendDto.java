package spharos.nu.notification.domain.notification.dto.request;

import lombok.*;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FcmSendDto {
    private List<String> tokens;

    private String title;

    private String content;

    private String link;


}
