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
import spharos.nu.read.domain.goods.dto.response.GoodsSummaryDto;
import spharos.nu.read.domain.goods.service.GoodsService;
import spharos.nu.read.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/read-n")
@Slf4j
@Tag(name = "GoodsN", description = "토큰을 검증이 필요없는 읽기용 굿즈 컨트롤러")
public class GoodsNController {

	private final GoodsService goodsService;

	// 상품 요약 조회
	@GetMapping("/{goodsCode}/summary")
	@Operation(summary = "굿즈 요약", description = "굿즈코드로 조회해야하는 상품들의 요약 데이터")
	public ResponseEntity<ApiResponse<GoodsSummaryDto>> getGoodsSummary(@PathVariable("goodsCode") String goodsCode) {

		return ApiResponse.success(goodsService.goodsSummaryGet(goodsCode), "상품 요약 조회 성공");
	}
}
