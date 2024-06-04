package spharos.nu.auth.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import spharos.nu.auth.domain.auth.entity.Member;

@Getter
public class UpdatePwdDto {

	@Schema(description = "현재 비밀번호")
	private String currentPassword;
	@Schema(description = "새로운 비밀번호")
	private String newPassword;

	public void updatePassword(Member member, String encodedNewPassword) {
		member.changePassword(encodedNewPassword);
	}
}
