package spharos.nu.etc.domain.notice.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.notice.dto.response.NoticeInfo;
import spharos.nu.etc.domain.notice.dto.response.NoticeResponseDto;
import spharos.nu.etc.domain.notice.service.NoticeService;
import spharos.nu.etc.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/etc-n/notice")
@Slf4j
@Tag(name = "NoticeN", description = "etc-service에서 토큰이 필요 없는 공지사항 관련 API document")
public class NoticeNController {

	private final NoticeService noticeService;

	@GetMapping("")
	@Operation(summary = "공지사항 조회", description = "모든 공지사항 조회")
	public ResponseEntity<ApiResponse<NoticeResponseDto>> getNotices(
		@PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC)
		Pageable pageable) {

		return ApiResponse.success(noticeService.noticesGet(pageable), "모든 공지사항 조회 성공");
	}

	@GetMapping("/{noticeId}")
	@Operation(summary = "공지 상세 조회", description = "해당 공지사항 자세히 보기")
	public ResponseEntity<ApiResponse<NoticeInfo>> getNotice(@PathVariable("noticeId") Long noticeId) {

		return ApiResponse.success(noticeService.noticeGet(noticeId), "공지사항 상세 조회 성공");
	}
}
