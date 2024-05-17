package spharos.nu.member.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.member.domain.member.dto.MannerDuckDto;
import spharos.nu.member.domain.member.dto.ProfileResponseDto;
import spharos.nu.member.domain.member.service.MyPageService;
import spharos.nu.member.global.apiresponse.ApiResponse;
import spharos.nu.member.utils.jwt.JwtProvider;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "MyPage", description = "member-service에서 마이페이지 관련 API document")
public class MyPageController {

	private final JwtProvider jwtProvider;
	private final MyPageService myPageService;

	// 프로필 조회
	@GetMapping("/users")
	@Operation(summary = "회원 프로필 조회", description = "회원 프로필이미지, 닉네임, 선호카테고리 데이터")
	public ResponseEntity<ApiResponse<ProfileResponseDto>> getProfile(@RequestHeader("Authorization") String token) {

		String uuid = jwtProvider.getUuid(token);

		return ApiResponse.success(myPageService.profileGet(uuid), "프로필 조회 성공");
	}

	// 매너덕 조회
	@GetMapping("/users/manner-duck")
	@Operation(summary = "회원의 매너덕 조회", description = "마이페이지의 매너덕 상태와 다음 매너덕까지 필요한 점수 데이터")
	public ResponseEntity<ApiResponse<MannerDuckDto>> getMannerDuck(@RequestHeader("Authorization") String token) {

		String uuid = jwtProvider.getUuid(token);

		return ApiResponse.success(myPageService.mannerDuckGet(uuid), "매너덕 조회 성공");
	}
}
