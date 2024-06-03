package spharos.nu.auth.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import spharos.nu.auth.domain.auth.entity.Member;

@Getter
public class ChangePwdDto {

	@Schema(description = "유저 조회에 사용할 핸드폰 번호")
	private String phoneNumber;
	@Schema(description = "새로운 비밀번호")
	private String newPassword;

	public void updatePassword(Member member, String encodedNewPassword) {
		member.changePassword(encodedNewPassword);
	}
}
