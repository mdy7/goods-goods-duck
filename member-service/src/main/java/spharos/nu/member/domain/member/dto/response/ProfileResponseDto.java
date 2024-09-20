package spharos.nu.member.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ProfileResponseDto {

	@Schema(description = "회원 uuid")
	private String userUuid;

	@Schema(description = "프로필 사진")
	private String profileImage;

	@Schema(description = "닉네임")
	private String nickname;

	@Schema(description = "선호 카테고리")
	private String favCategory;
}
