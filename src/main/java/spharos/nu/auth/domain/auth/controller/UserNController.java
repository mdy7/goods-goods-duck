package spharos.nu.auth.domain.auth.controller;

import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.auth.domain.auth.dto.request.ResetPwdDto;
import spharos.nu.auth.domain.auth.dto.request.JoinDto;
import spharos.nu.auth.domain.auth.dto.request.LoginDto;
import spharos.nu.auth.domain.auth.dto.request.SocialLoginDto;
import spharos.nu.auth.domain.auth.dto.response.LoginResponseDto;
import spharos.nu.auth.domain.auth.service.UserService;
import spharos.nu.auth.global.apiresponse.ApiResponse;
import spharos.nu.auth.utils.jwt.JwtProvider;
import spharos.nu.auth.utils.jwt.JwtToken;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth-n")
@Slf4j
@Tag(name = "Users", description = "회원가입 및 로그인 등등 유저 관련 인증과 관련하여 기본적으로 필요한 메소드")
public class UserNController {
	private final UserService userService;

	@PostMapping("/login")
	@Operation(summary = "로그인")
	public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginDto loginDto) {
		return ApiResponse.success(userService.login(loginDto), "로그인에 성공했습니다.");
	}

	@PostMapping("/social-login")
	@Operation(summary = "소셜 로그인", description = "소셜로 가입된 유저라면 토큰 발급, 아니라면 회원가입 페이지로 리다이렉트")
	public ResponseEntity<ApiResponse<LoginResponseDto>> socialLogin(@RequestBody SocialLoginDto socialLoginDto) {

		return ApiResponse.success(userService.socialLogin(socialLoginDto), "로그인에 성공했습니다.");
	}

	@PostMapping()
	@Operation(summary = "회원가입")
	public ResponseEntity<ApiResponse<Void>> join(@RequestBody JoinDto joinDto) {
		userService.join(joinDto);
		return ApiResponse.created("회원가입에 성공했습니다.");
	}

	@GetMapping("/duplication-check")
	@Operation(summary = "아이디 중복체크", description = "아이디가 중복되었다면 409 error, 아니면 200 ok")
	public ResponseEntity<ApiResponse<String>> duplicationCheck(@RequestParam String inputParams) {
		userService.isDuplicatedId(inputParams);
		return ApiResponse.success(inputParams, "사용 가능한 아이디입니다.");
	}

	@GetMapping("/find/{tab}")
	@Operation(summary = "아이디/비밀번호 찾기", description = "핸드폰 번호 활용해서 아이디 찾기, 없는 정보면 404 error")
	public ResponseEntity<ApiResponse<Optional<String>>> findUser(@PathVariable String tab, @RequestParam String inputParams) {
		if (Objects.equals(tab, "id")) {
			String userId = userService.findId(inputParams);
			return ApiResponse.success(Optional.of(userId), "아이디 조회에 성공했습니다.");
		} else if (Objects.equals(tab, "pw")) {
			userService.findPwd(inputParams);
			return ApiResponse.see_other("비밀번호 재설정 페이지로 이동하세요.");
		} else {
			return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다.");
		}
	}

	@PutMapping("/pwd")
	@Operation(summary = "비밀번호 초기화", description = "이전 비밀번호와 같다면 409 error")
	public ResponseEntity<ApiResponse<Void>> changePwd(@RequestBody ResetPwdDto resetPwdDto) {
		userService.resetPwd(resetPwdDto);
		return ApiResponse.success(null, "정보 수정이 완료됐습니다.");
	}
}
