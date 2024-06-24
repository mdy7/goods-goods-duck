package spharos.nu.etc.domain.report.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.report.dto.request.ReportRequestDto;
import spharos.nu.etc.domain.report.dto.response.GoodsReportResponseDto;
import spharos.nu.etc.domain.report.dto.response.UserReportResponseDto;
import spharos.nu.etc.domain.report.service.ReportService;
import spharos.nu.etc.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/etc/reports")
@Slf4j
@Tag(name = "Report", description = "etc-service에서 토큰이 필요한 신고 관련 API document")
public class ReportController {

	private final ReportService reportService;

	// 굿즈 신고
	@PostMapping("/goods/{goodsCode}")
	@Operation(summary = "굿즈 신고", description = "굿즈 신고를 할 수 있는 api")
	public ResponseEntity<ApiResponse<Void>> reportGoods(
		@RequestHeader(value = "User-Uuid", required = false) String reporterUuid,
		@PathVariable("goodsCode") String goodsCode,
		@RequestBody ReportRequestDto reportRequestDto) {

		return ApiResponse.success(reportService.goodsReport(reporterUuid, goodsCode, reportRequestDto),
			"굿즈 신고하기 성공");
	}

	// 유저 신고
	@PostMapping("users/{reportedUuid}")
	@Operation(summary = "유저 신고", description = "유저 신고를 할 수 있는 api")
	public ResponseEntity<ApiResponse<Void>> reportUser(
		@RequestHeader(value = "User-Uuid", required = false) String repoterUuid,
		@PathVariable("reportedUuid") String reportedUuid, @RequestBody ReportRequestDto ReportRequestDto) {

		return ApiResponse.success(reportService.userReport(repoterUuid, reportedUuid, ReportRequestDto),
			"유저 신고하기 성공");
	}

	// 굿즈 신고 조회
	@GetMapping("/goods")
	@Operation(summary = "모든 신고 조회", description = "굿즈 신고 조회")
	public ResponseEntity<ApiResponse<GoodsReportResponseDto>> getGoodsReport(
		@PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

		return ApiResponse.success(reportService.goodsReportGet(pageable), "모든 굿즈 조회 성공");
	}

	// 회원 신고 조회
	@GetMapping("/users")
	@Operation(summary = "모든 신고 조회", description = "회원 신고 조회")
	public ResponseEntity<ApiResponse<UserReportResponseDto>> getUserReport(
		@PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

		return ApiResponse.success(reportService.userReportGet(pageable), "모든 회원 신고 조회 성공");
	}
}
