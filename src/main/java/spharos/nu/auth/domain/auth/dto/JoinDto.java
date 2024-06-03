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
	@Schema(description = "닉네임")
	private String nickname;
	@Schema(description = "프로필 사진")
	private String profileImage;
	@Schema(description = "선호 카테고리")
	private String favoriteCategory;

	public Member toEntity(String uuid, String encodedPassword) {
		return Member.builder()
			.uuid(uuid)
			.userId(this.userId)
			.password(encodedPassword)
			.phoneNumber(this.phoneNumber)
			.nickname(this.nickname)
			.profileImage(this.profileImage)
			.favoriteCategory(this.favoriteCategory)
			.isNotify(true)
			.isWithdraw(false)
			.build();
	}
}
