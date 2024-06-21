package spharos.nu.auth.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class VerificationDto {
	@Schema(description = "핸드폰 번호")
	private String phoneNumber;
	@Schema(description = "인증번호")
	private String verificationNumber;
	@Schema(description = "소셜 id")
	private String memberCode;
	@Schema(description = "소셜 로그인 provider")
	private String provider;
}
