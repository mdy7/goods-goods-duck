package spharos.nu.auth.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
	@Schema(description = "유저 uuid")
	private String uuid;
	@Schema(description = "액세스 토큰")
	private String accessToken;
	@Schema(description = "리프레시 토큰")
	private String refreshToken;
}
