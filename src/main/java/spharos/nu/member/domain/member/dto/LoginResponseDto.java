package spharos.nu.member.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import spharos.nu.member.utils.jwt.JwtToken;

@Getter
@Builder
public class LoginResponseDto {
	private JwtToken jwtToken;
	private String nickname;
	private String profileImage;
	private String favoriteCategory;
}
