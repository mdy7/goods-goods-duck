package spharos.nu.member.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import spharos.nu.member.domain.member.dto.VerificationDto;
import spharos.nu.member.domain.member.service.VerificationService;
import spharos.nu.member.global.apiresponse.ApiResponse;

@RequiredArgsConstructor
public class VerificationController {

	private final VerificationService verificationService;

	@PostMapping("/verification")
	@Operation(summary = "회원가입용 휴대폰 인증 문자 발송", description = "이미 회원인 경우 409 error")
	public ResponseEntity<ApiResponse<Void>> joinVerify(@RequestBody VerificationDto verificationDto) {
		verificationService.sendJoinSms(verificationDto);

		return ApiResponse.success(null, "인증번호가 발송되었습니다.");
	}

	@PostMapping("/verification/confirm")
	@Operation(summary = "회원가입용 휴대폰 인증번호 확인", description = "3분의 유효 시간이 지나면 인증번호 만료")
	public ResponseEntity<ApiResponse<Void>> joinConfirm(@RequestBody VerificationDto verificationDto) {
		verificationService.verifySms(verificationDto);

		return ApiResponse.success(null, "휴대폰 인증에 성공했습니다.");
	}
}
