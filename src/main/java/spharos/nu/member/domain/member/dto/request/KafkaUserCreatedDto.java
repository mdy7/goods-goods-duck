package spharos.nu.member.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KafkaUserCreatedDto {
	@Schema(description = "유저 uuid")
	private String uuid;
	@Schema(description = "유저 닉네임")
	private String nickname;
	@Schema(description = "유저 프로필 사진")
	private String profileImage;
	@Schema(description = "선호 카테고리")
	private String favoriteCategory;
}
