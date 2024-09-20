package spharos.nu.member.domain.member.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.member.domain.member.dto.response.AllMemberResponseDto;
import spharos.nu.member.domain.member.service.AdminService;
import spharos.nu.member.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users-n/admin")
@Slf4j
@Tag(name = "Admin", description = "member-service에서 어드민 관련 API document")
public class AdminController {

	private final AdminService adminService;

	// 전체 회원 조회
	@GetMapping("/all-member")
	@Operation(summary = "회원 전체 조회", description = "전체 회원의 프로필 데이터, 블랙 회원 필터링")
	public ResponseEntity<ApiResponse<AllMemberResponseDto>> getAllMember(
		@RequestParam(name = "isBlack", defaultValue = "false") boolean isBlack,
		@PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

		return ApiResponse.success(adminService.allMemberGet(pageable, isBlack), "전체 회원 조회 성공");
	}
}
