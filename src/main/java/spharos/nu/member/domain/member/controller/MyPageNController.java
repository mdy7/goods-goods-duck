package spharos.nu.member.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import spharos.nu.member.domain.member.dto.response.MannerDuckDto;
import spharos.nu.member.domain.member.service.MyPageService;
import spharos.nu.member.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-n")
public class MyPageNController {
	private final MyPageService myPageService;

	@GetMapping("/duplication-check")
	@Operation(summary = "닉네임 중복체크", description = "닉네임이 중복되었다면 409 error, 아니면 200 ok")
	public ResponseEntity<ApiResponse<String>> duplicationCheck(@RequestParam String inputParams) {
		myPageService.isDuplicatedNick(inputParams);
		return ApiResponse.success(inputParams, "사용 가능한 닉네임입니다.");
	}

	// 매너덕 조회
	@GetMapping("/{userUuid}/manner-duck")
	@Operation(summary = "회원의 매너덕 조회", description = "마이페이지의 매너덕 상태와 다음 매너덕까지 필요한 점수 데이터")
	public ResponseEntity<ApiResponse<MannerDuckDto>> getMannerDuck(
		@PathVariable("userUuid") String userUuid) {

		return ApiResponse.success(myPageService.mannerDuckGet(userUuid), "매너덕 조회 성공");
	}
}
