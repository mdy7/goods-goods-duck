package spharos.nu.member.domain.Payment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class KakaoPayReadyResponseDto {
    private String tid;                  //결제 고유 번호
    private String next_redirect_pc_url; // 리닥이렉트 url
}
