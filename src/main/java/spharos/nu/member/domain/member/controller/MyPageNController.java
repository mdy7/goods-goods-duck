package spharos.nu.member.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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
}
