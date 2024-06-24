package spharos.nu.etc.domain.admin.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.admin.dto.request.NoticeRequestDto;
import spharos.nu.etc.domain.admin.dto.response.NoticeResponseDto;
import spharos.nu.etc.domain.admin.service.AdminService;
import spharos.nu.etc.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/etc/admin")
@Slf4j
@Tag(name = "Admin", description = "etc-service에서 토큰이 필요한 어드민 관련 API document")
public class AdminController {

	private final AdminService adminService;

	@GetMapping("/notice")
	@Operation(summary = "공지사항 조회", description = "모든 공지사항 조회")
	public ResponseEntity<ApiResponse<NoticeResponseDto>> getNotice(
		@PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC)
		Pageable pageable) {

		return ApiResponse.success(adminService.noticeGet(pageable), "공지사항 조회 성공");
	}

	@PostMapping("/notice")
	@Operation(summary = "공지사항 등록", description = "어드민에서 등록할 수 있는 공지사항")
	public ResponseEntity<ApiResponse<Long>> createNotice(@RequestBody NoticeRequestDto noticeRequestDto) {

		return ApiResponse.success(adminService.noticeCreate(noticeRequestDto), "공지사항 등록 성공");
	}
}
