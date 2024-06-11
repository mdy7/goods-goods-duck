package spharos.nu.member.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.member.domain.member.dto.request.ProfileImageRequestDto;
import spharos.nu.member.domain.member.dto.request.ProfileRequestDto;
import spharos.nu.member.domain.member.dto.response.DuckPointDetailDto;
import spharos.nu.member.domain.member.dto.response.MannerDuckDto;
import spharos.nu.member.domain.member.dto.response.ProfileResponseDto;
import spharos.nu.member.domain.member.service.MyPageService;
import spharos.nu.member.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
@Tag(name = "MyPage", description = "member-service에서 마이페이지 관련 API document")
public class MyPageController {

	private final MyPageService myPageService;

	// 프로필 수정
	@PatchMapping("")
	@Operation(summary = "회원 프로필 수정", description = "회원 프로필이미지, 닉네임, 선호카테고리 수정")
	public ResponseEntity<ApiResponse<Void>> updateProfile(
		@RequestHeader(value = "User-Uuid", required = false) String uuid,
		@RequestBody ProfileRequestDto profileRequestDto) {

		return ApiResponse.success(myPageService.profileUpdate(uuid, profileRequestDto), "프로필 수정 성공");
	}

	// 프로필 사진 조회
	@GetMapping("/profile-img")
	@Operation(summary = "회원 프로필 이미지 조회", description = "회원 프로필이미지 데이터")
	public ResponseEntity<ApiResponse<String>> getProfileImage(
		@RequestHeader(value = "User-Uuid", required = false) String uuid) {

		return ApiResponse.success(myPageService.profileImageGet(uuid), "프로필 이미지 조회 성공");
	}

	// 프로필 사진 수정
	@PatchMapping("/profile-img")
	@Operation(summary = "회원 프로필 이미지 수정", description = "회원 프로필이미지 수정")
	public ResponseEntity<ApiResponse<String>> updateProfileImage(
		@RequestHeader(value = "User-Uuid", required = false) String uuid,
		@RequestBody ProfileImageRequestDto profileImageRequestDto) {

		return ApiResponse.success(myPageService.profileImageUpdate(uuid, profileImageRequestDto), "프로필 이미지 수정 성공");
	}

	// 프로필 사진 삭제
	@DeleteMapping("/profile-img")
	@Operation(summary = "회원 프로필 이미지 삭제", description = "회원 프로필 이미지 삭제")
	public ResponseEntity<ApiResponse<Void>> deleteProfileImage(
		@RequestHeader(value = "User-Uuid", required = false) String uuid) {

		return ApiResponse.success(myPageService.profileImageDelete(uuid), "프로필이미지 삭제 성공");
	}

	// 매너덕 조회
	@GetMapping("/manner-duck")
	@Operation(summary = "회원의 매너덕 조회", description = "마이페이지의 매너덕 상태와 다음 매너덕까지 필요한 점수 데이터")
	public ResponseEntity<ApiResponse<MannerDuckDto>> getMannerDuck(
		@RequestHeader(value = "User-Uuid", required = false) String uuid) {

		return ApiResponse.success(myPageService.mannerDuckGet(uuid), "매너덕 조회 성공");
	}

	// 덕포인트 조회
	@GetMapping("/duck-point")
	@Operation(summary = "회원의 덕포인트 조회", description = "마이페이지의 현재 덕포인트 데이터")
	public ResponseEntity<ApiResponse<Long>> getDuckPoing(
		@RequestHeader(value = "User-Uuid", required = false) String uuid) {

		return ApiResponse.success(myPageService.duckPointGet(uuid), "덕포인트 조회 성공");
	}

	// 덕포인트 상세 조회
	@GetMapping("/manner-duck/detail")
	@Operation(summary = "회원의 덕포인트 내역 조회", description = "덕포인트 내역과 관련한 변동금액, 잔여포인트, 입출금여부, 내역상세, 생성날짜 데이터")
	public ResponseEntity<ApiResponse<DuckPointDetailDto>> getDuckPointDetail(
		@RequestHeader(value = "User-Uuid", required = false) String uuid,
		@RequestParam(value = "page", defaultValue = "0") Integer index) {

		return ApiResponse.success(myPageService.duckPointDetailGet(uuid, index), "덕포인트 상세 내역 조회 성공");
	}
}