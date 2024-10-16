package spharos.nu.auth.domain.auth.kafka.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinResponseDto {
	@Schema(description = "유저 uuid")
	private String uuid;
	@Schema(description = "유저 닉네임")
	private String nickname;
	@Schema(description = "유저 프로필 사진")
	private String profileImage;
	@Schema(description = "선호 카테고리")
	private String favoriteCategory;
}

