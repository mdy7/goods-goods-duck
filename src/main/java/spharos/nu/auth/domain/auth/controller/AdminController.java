package spharos.nu.auth.domain.auth.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.auth.domain.auth.dto.response.WithdrawMemberResponseDto;
import spharos.nu.auth.domain.auth.service.AdminService;
import spharos.nu.auth.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth-n/admin")
@Slf4j
@Tag(name = "Admin", description = "auth-service에서 어드민 관련 API document")
public class AdminController {

	private final AdminService adminService;

	@GetMapping("/withdraw-member")
	@Operation(summary = "탈퇴 회원 조회", description = "탈퇴 회원 관리를 위한 탈퇴 회원 조회")
	public ResponseEntity<ApiResponse<WithdrawMemberResponseDto>> getWithdrawMember(
		@PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC)
		Pageable pageable) {

		return ApiResponse.success(adminService.withdrawMemberGet(pageable), "탈퇴 회원 조회 성공");
	}
}
