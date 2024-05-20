package spharos.nu.member.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class VerificationDto {
	@Schema(description = "핸드폰 번호")
	private String phoneNumber;
	@Schema(description = "인증번호")
	private String verificationNumber;
}
