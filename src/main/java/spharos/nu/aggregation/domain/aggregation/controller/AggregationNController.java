package spharos.nu.aggregation.domain.aggregation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/{goodsCode}/wish-count")
	@Operation(summary = "좋아요수 조회", description = "상품코드로 좋아요수를 조회합니다")
	public ResponseEntity<ApiResponse<Long>> getWishCount(
		@PathVariable(value = "goodsCode") String goodsCode
	) {
		return ApiResponse.success(aggregationService.getWishCount(goodsCode),"좋아요수 조회 성공");
	}
}
