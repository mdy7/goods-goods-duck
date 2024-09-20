package spharos.nu.member.domain.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoPayReadyRequestDto {
    @Schema(description = "결제명")
    private String itemName;

    @Schema(description = "결제금액")
    private String totalAmount;

    @Schema(description = "회원식별자")
    private String uuid;


}
