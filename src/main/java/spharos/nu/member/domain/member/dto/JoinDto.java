package spharos.nu.member.domain.member.dto;

import lombok.Getter;
import spharos.nu.member.domain.member.entity.Member;

@Getter
public class JoinDto {
	private String userId;
	private String password;
	private String phoneNumber;
	private String nickname;
	private String profileImage;
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
