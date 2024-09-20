package spharos.nu.auth.utils.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import spharos.nu.auth.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt")
public class JwtController {
	@Value("${jwt.token.refresh-expire-time}")
	private Long refreshExpireTime;
	private final JwtProvider jwtProvider;

	@GetMapping("/re-issue")
	@Operation(summary = "토큰 재발급", description = "AccessToken 재발급, RefreshToken 갱신")
	public ResponseEntity<ApiResponse<JwtToken>> reIssueToken(String refreshToken) {
		JwtToken newToken = jwtProvider.reIssueToken(refreshToken);
		return ApiResponse.success(newToken, "토큰 재발급 성공");
	}
}
