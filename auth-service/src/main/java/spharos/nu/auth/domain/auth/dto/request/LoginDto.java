package spharos.nu.auth.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginDto {
	@Schema(description = "유저 아이디")
	private String userId;
	@Schema(description = "비밀번호")
	private String password;
}
