package spharos.nu.auth.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinDto {
	@Schema(description = "유저 아이디")
	private String userId;
	@Schema(description = "비밀번호")
	private String password;
	@Schema(description = "핸드폰 번호")
	private String phoneNumber;
	@Schema(description = "유저 닉네임")
	private String nickname;
	@Schema(description = "유저 프로필 사진")
	private String profileImage;
	@Schema(description = "선호 카테고리")
	private String favoriteCategory;
}
