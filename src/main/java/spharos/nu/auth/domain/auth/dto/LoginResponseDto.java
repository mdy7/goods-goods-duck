package spharos.nu.auth.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;
import spharos.nu.auth.utils.jwt.JwtToken;

@Getter
@Builder
public class LoginResponseDto {
	private JwtToken jwtToken;
	private String nickname;
	private String profileImage;
	private String favoriteCategory;
}
