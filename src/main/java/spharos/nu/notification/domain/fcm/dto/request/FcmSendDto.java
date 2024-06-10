package spharos.nu.notification.domain.fcm.dto.request;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FcmSendDto {
    private String token;

    private String title;

    private String content;


}
