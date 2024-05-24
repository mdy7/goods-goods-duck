package spharos.nu.member.domain.member.controller;

import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import spharos.nu.member.domain.member.dto.ChangePwdDto;
import spharos.nu.member.domain.member.dto.JoinDto;
import spharos.nu.member.domain.member.dto.LoginDto;
import spharos.nu.member.domain.member.dto.SocialLoginDto;
import spharos.nu.member.domain.member.service.UserService;
import spharos.nu.member.global.apiresponse.ApiResponse;
import spharos.nu.member.utils.jwt.JwtProvider;
import spharos.nu.member.utils.jwt.JwtToken;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/users-n")
@Tag(name = "Users", description = "회원가입 및 로그인 등등 유저 관련 기본적으로 필요한 메소드")
public class UserController {
	private final UserService userService;
	private final JwtProvider jwtProvider;

	@PostMapping("/login")
	@Operation(summary = "로그인")
	public ResponseEntity<ApiResponse<JwtToken>> login(@RequestBody LoginDto loginDto) {
		JwtToken tokens = userService.login(loginDto);

		return ApiResponse.success(tokens, "로그인에 성공했습니다.");
	}

	@PostMapping("/social-login")
	@Operation(summary = "소셜 로그인", description = "소셜로 가입된 유저라면 토큰 발급, 아니라면 회원가입 페이지로 리다이렉트")
	public ResponseEntity<ApiResponse<JwtToken>> socialLogin(@RequestBody SocialLoginDto socialLoginDto) {
		JwtToken tokens = userService.socialLogin(socialLoginDto);

		return ApiResponse.success(tokens, "로그인에 성공했습니다.");
	}

	@PostMapping()
	@Operation(summary = "회원가입")
	public ResponseEntity<ApiResponse<Void>> join(@RequestBody JoinDto joinDto) {
		userService.join(joinDto);
		return ApiResponse.created("회원가입에 성공했습니다.");
	}

	@GetMapping("/duplication-check/{type}")
	@Operation(summary = "아이디 중복체크", description = "아이디/닉네임이 중복되었다면 409 error, 아니면 200 ok")
	public ResponseEntity<ApiResponse<String>> duplicationCheck(@PathVariable String type, @RequestParam String inputParams) {
		if (Objects.equals(type, "id")) {
			userService.isDuplicatedId(inputParams);
			return ApiResponse.success(inputParams, "사용 가능한 아이디입니다.");
		} else if (Objects.equals(type, "nick")) {
			userService.isDuplicatedNick(inputParams);
			return ApiResponse.success(inputParams, "사용 가능한 닉네임입니다.");
		} else {
			return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다.");
		}
	}

	@GetMapping("/find/{tab}")
	@Operation(summary = "아이디/비밀번호 찾기", description = "닉네임 활용해서 아이디 찾기, 없는 정보면 404 error")
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
	@Operation(summary = "비밀번호 재설정", description = "이전 비밀번호와 같다면 409 error")
	public ResponseEntity<ApiResponse<Void>> changePwd(@RequestBody ChangePwdDto changePwdDto) {
		userService.changePwd(changePwdDto);
		return ApiResponse.success(null, "정보 수정이 완료됐습니다.");
	}
}
