package spharos.nu.aggregation.domain.aggregation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import spharos.nu.aggregation.domain.aggregation.service.AggregationService;
import spharos.nu.aggregation.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/aggregation-n")
public class AggregationNController {

	private final AggregationService aggregationService;

	@PostMapping("/views/{goodsCode}")
	@Operation(summary = "조회수 카운팅", description = "조회수를 +1 합니다")
	public ResponseEntity<ApiResponse<Void>> addViewsCount(
		@PathVariable(value = "goodsCode") String goodsCode
	) {
		aggregationService.addViewsCount(goodsCode);
		return ApiResponse.success(null,"조회수 추가 성공");
	}

	@PostMapping("/bid/{goodsCode}")
	@Operation(summary = "입찰수 카운팅", description = "입찰수를 +1 합니다")
	public ResponseEntity<ApiResponse<Void>> addBidCount(
		@PathVariable(value = "goodsCode") String goodsCode
	) {
		aggregationService.addBidCount(goodsCode);
		return ApiResponse.success(null,"입찰수 추가 성공");
	}
}
