package spharos.nu.etc.domain.report.controller;

import org.springframework.http.ResponseEntity;
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
import spharos.nu.etc.domain.report.dto.request.GoodsReportRequestDto;
import spharos.nu.etc.domain.report.service.ReportService;
import spharos.nu.etc.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/etc/reports")
@Slf4j
@Tag(name = "Report", description = "etc-service에서 토큰이 필요한 신고 관련 API document")
public class ReportController {

	private final ReportService reportService;

	@PostMapping("/{goodsCode}")
	@Operation(summary = "굿즈 신고", description = "굿즈 신고를 할 수 있는 api")
	public ResponseEntity<ApiResponse<Void>> reportGoods(
		@RequestHeader(value = "User-Uuid", required = false) String reporterUuid,
		@PathVariable("goodsCode") String goodsCode,
		@RequestBody GoodsReportRequestDto goodsReportRequestDto) {

		return ApiResponse.success(reportService.goodsReport(reporterUuid, goodsCode, goodsReportRequestDto), "굿즈 신고하기 성공");
	}
}
