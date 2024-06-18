package spharos.nu.read.domain.goods.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.read.domain.goods.service.AggregationService;
import spharos.nu.read.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/read-n")
@Slf4j
@Tag(name = "AggregationN", description = "토큰 검증이 필요없는 읽기용 굿즈 집계 조회 컨트롤러")
public class AggregationNController {

	private final AggregationService aggregationService;

	@GetMapping("/{goodsCode}/wish-count")
	@Operation(summary = "좋아요수 조회", description = "굿즈코드로 해당 굿즈의 좋아요수를 조회합니다")
	public ResponseEntity<ApiResponse<Long>> getWishCount(@PathVariable("goodsCode") String goodsCode) {
		return ApiResponse.success(aggregationService.getWishCount(goodsCode),"좋아요수 조회 성공");
	}

	@GetMapping("/{goodsCode}/views-count")
	@Operation(summary = "조회수 조회", description = "굿즈코드로 해당 굿즈의 조회수를 조회합니다")
	public ResponseEntity<ApiResponse<Long>> getViewsCount(@PathVariable("goodsCode") String goodsCode) {
		return ApiResponse.success(aggregationService.getViewsCount(goodsCode),"조회수 조회 성공");
	}

	@GetMapping("/{goodsCode}/bid-count")
	@Operation(summary = "입찰수 조회", description = "굿즈코드로 해당 굿즈의 입찰수를 조회합니다")
	public ResponseEntity<ApiResponse<Long>> getBidCount(@PathVariable("goodsCode") String goodsCode) {
		return ApiResponse.success(aggregationService.getBidCount(goodsCode),"입찰수 조회 성공");
	}
}
