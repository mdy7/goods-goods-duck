package spharos.nu.member.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class KakaoPayReadyResponseDto {
    private String tid;
    private String next_redirect_pc_url;
    private String next_redirect_mobile_url;
    private String next_redirect_app_url;
}
