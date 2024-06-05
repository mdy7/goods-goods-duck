package spharos.nu.member.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spharos.nu.member.domain.member.entity.MemberInfo;

@Getter
@NoArgsConstructor
public class ProfileRequestDto {

	@Schema(description = "프로필이미지")
	private String profileImage;

	@Schema(description = "닉네임")
	private String nickname;

	@Schema(description = "선호 카테고리")
	private String favoriteCategory;

	public ProfileRequestDto(String profileImage, String nickname, String favoriteCategory) {

		this.profileImage = profileImage;
		this.nickname = nickname;
		this.favoriteCategory = favoriteCategory;
	}
}
