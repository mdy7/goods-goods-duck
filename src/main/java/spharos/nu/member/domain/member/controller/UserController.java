package spharos.nu.member.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import spharos.nu.member.domain.member.dto.LoginDto;
import spharos.nu.member.domain.member.service.UserService;
import spharos.nu.member.global.apiresponse.ApiResponse;
import spharos.nu.member.utils.jwt.JwtToken;

@RestController
@RequiredArgsConstructor
@Tag(name = "Users", description = "회원가입 및 로그인 등등 유저 관련 기본적으로 필요한 메소드")
public class UserController {
	private final UserService userService;

	@PostMapping("/login")
	@Operation(summary = "로그인")
	public ResponseEntity<ApiResponse<JwtToken>> login(@RequestBody LoginDto loginDto) {
		JwtToken tokens = userService.login(loginDto);

		return ApiResponse.success(tokens, "로그인에 성공했습니다.");
	}
}
