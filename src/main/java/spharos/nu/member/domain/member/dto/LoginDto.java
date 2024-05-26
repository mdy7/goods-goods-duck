package spharos.nu.member.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class LoginDto {
	@Schema(description = "유저 아이디")
	private String userId;
	@Schema(description = "비밀번호")
	private String password;
}
