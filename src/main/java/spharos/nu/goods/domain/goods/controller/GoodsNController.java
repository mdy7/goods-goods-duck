package spharos.nu.goods.domain.goods.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import spharos.nu.goods.domain.goods.dto.request.ImageDto;
import spharos.nu.goods.domain.goods.dto.request.TagDto;
import spharos.nu.goods.domain.goods.dto.response.GoodsAllListDto;
import spharos.nu.goods.domain.goods.dto.response.GoodsDetailDto;
import spharos.nu.goods.domain.goods.dto.response.GoodsSummaryDto;
import spharos.nu.goods.domain.goods.service.GoodsService;
import spharos.nu.goods.global.apiresponse.ApiResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/goods-n")
@Tag(name = "GoodsN", description = "토큰 검증이 필요없는 굿즈 컨트롤러")
public class GoodsNController {
	private final GoodsService goodsService;

	@Operation(summary = "전체 굿즈 조회", description = "전체 굿즈를 조회합니다. sort 에 들어갈 유효한 값은 createdAt, closedAt 입니다")
	@GetMapping()
	public ResponseEntity<ApiResponse<GoodsAllListDto>> getAllGoods(
		@RequestParam(value = "categoryPk", defaultValue = "0") Long categoryPk,
		@RequestParam(value = "isTradingOnly", defaultValue = "false") boolean isTradingOnly,
		@PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return ApiResponse.success(goodsService.goodsAllRead(categoryPk,isTradingOnly,pageable), "전체 조회 성공");
	}

	@Operation(summary = "굿즈 상세 조회", description = "굿즈의 상세정보를 조회합니다")
	@GetMapping("/{goodsCode}")
	public ResponseEntity<ApiResponse<GoodsDetailDto>> getGoodsDetail(
		@PathVariable("goodsCode") String goodsCode
	) {
		return ApiResponse.success(goodsService.getGoodsDetail(goodsCode), "굿즈 상세조회 성공");
	}

	@Operation(summary = "굿즈 요약 조회", description = "굿즈의 요약정보를 조회합니다")
	@GetMapping("/{goodsCode}/summary")
	public ResponseEntity<ApiResponse<GoodsSummaryDto>> getGoodsSummary(
		@PathVariable("goodsCode") String goodsCode
	) {
		return ApiResponse.success(goodsService.getGoodsSummary(goodsCode), "굿즈 요약조회 성공");
	}

	@Operation(summary = "굿즈 거래상태 조회", description = "굿즈의 거래상태를 조회합니다")
	@GetMapping("/{goodsCode}/trading-status")
	public ResponseEntity<ApiResponse<Byte>> getGoodsTradingStatus(
		@PathVariable("goodsCode") String goodsCode
	) {
		return ApiResponse.success(goodsService.getGoodsTradingStatus(goodsCode), "굿즈 거래상태 조회 성공");
	}

}
