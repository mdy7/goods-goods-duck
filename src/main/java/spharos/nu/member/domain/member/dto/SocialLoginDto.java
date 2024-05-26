package spharos.nu.member.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SocialLoginDto {
	@Schema(description = "소셜 로그인 시 발급되는 소셜코드")
	private String memberCode;
}
