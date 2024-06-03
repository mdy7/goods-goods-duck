package spharos.nu.auth.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import spharos.nu.auth.domain.auth.entity.Member;

@Getter
public class JoinDto {
	@Schema(description = "유저 아이디")
	private String userId;
	@Schema(description = "비밀번호")
	private String password;
	@Schema(description = "핸드폰 번호")
	private String phoneNumber;
}
