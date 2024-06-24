package spharos.nu.etc.domain.notice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.notice.dto.request.NoticeRequestDto;
import spharos.nu.etc.domain.notice.service.NoticeService;
import spharos.nu.etc.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/etc/notice")
@Slf4j
@Tag(name = "Notice", description = "etc-service에서 토큰이 필요한 공지사항 관련 API document")
public class NoticeController {

	private final NoticeService noticeService;

	// 공지사항 등록
	@PostMapping("")
	@Operation(summary = "공지사항 등록", description = "어드민에서 등록할 수 있는 공지사항")
	public ResponseEntity<ApiResponse<Long>> createNotice(@RequestBody NoticeRequestDto noticeRequestDto) {

		return ApiResponse.success(noticeService.noticeCreate(noticeRequestDto), "공지사항 등록 성공");
	}
}
