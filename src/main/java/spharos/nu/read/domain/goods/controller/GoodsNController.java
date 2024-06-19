package spharos.nu.read.domain.goods.controller;

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
import lombok.extern.slf4j.Slf4j;
import spharos.nu.read.domain.goods.dto.response.AllGoodsDto;
import spharos.nu.read.domain.goods.dto.response.GoodsDetailDto;
import spharos.nu.read.domain.goods.dto.response.GoodsSummaryDto;
import spharos.nu.read.domain.goods.entity.Goods;
import spharos.nu.read.domain.goods.service.GoodsService;
import spharos.nu.read.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/read-n")
@Slf4j
@Tag(name = "GoodsN", description = "토큰 검증이 필요없는 읽기용 굿즈 정보 조회 컨트롤러")
public class GoodsNController {

	private final GoodsService goodsService;

	// 상품 요약 조회
	@GetMapping("/{goodsCode}/summary")
	@Operation(summary = "굿즈 요약", description = "굿즈코드로 조회해야하는 상품들의 요약 데이터")
	public ResponseEntity<ApiResponse<GoodsSummaryDto>> getGoodsSummary(@PathVariable("goodsCode") String goodsCode) {

		return ApiResponse.success(goodsService.goodsSummaryGet(goodsCode), "상품 요약 조회 성공");
	}

	// 상품 상세 조회
	@GetMapping("/{goodsCode}/detail")
	@Operation(summary = "굿즈 상세", description = "굿즈코드로 조회해야하는 상품들의 상세 데이터")
	public ResponseEntity<ApiResponse<GoodsDetailDto>> getGoodsDetail(@PathVariable("goodsCode") String goodsCode) {

		return ApiResponse.success(goodsService.getGoodsDetail(goodsCode), "상품 상세 조회 성공");
	}

	// 전체 굿즈 리스트 조회
	@GetMapping("/{goodsCode}/all-list")
	@Operation(summary = "전체 굿즈 조회", description = "sort 에 들어갈 유효한 값은 createdAt, closedAt, wishCount, viewsCount, bidCount + ,ASC 또는 ,DESC  입니다. ex) 최신순 -> 'createdAt,DESC'")
	public ResponseEntity<ApiResponse<AllGoodsDto>> getAllGoods(
		@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
		@RequestParam(value = "isTradingOnly", defaultValue = "false") boolean isTradingOnly,
		@PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return ApiResponse.success(goodsService.getAllGoods(categoryId,isTradingOnly,pageable), "전체 굿즈 리스트 조회 성공");
	}

	// 현재 경매중인 굿즈 리스트 조회
	@GetMapping("/{goodsCode}/now-trading")
	@Operation(summary = "경매중 굿즈 조회", description = "현재 경매중인 굿즈를 조회합니다")
	public ResponseEntity<ApiResponse<List<GoodsSummaryDto>>> getNowTradingGoods(
		@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
		@PageableDefault(size = 10, page = 0, sort = "closedAt", direction = Sort.Direction.ASC) Pageable pageable
	) {
		return ApiResponse.success(goodsService.getNowTradingGoods(categoryId,pageable), "경매중 굿즈 리스트 조회 성공");
	}

	// 커밍순 굿즈 리스트 조회
	@GetMapping("/{goodsCode}/coming-soon")
	@Operation(summary = "커밍순 굿즈 조회", description = "커밍순 예정 굿즈를 조회합니다")
	public ResponseEntity<ApiResponse<List<GoodsSummaryDto>>> getComingSoonGoods(
		@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
		@PageableDefault(size = 10, page = 0, sort = "openedAt", direction = Sort.Direction.ASC) Pageable pageable
	) {
		return ApiResponse.success(goodsService.getComingSoonGoods(categoryId,pageable), "경매중 굿즈 리스트 조회 성공");
	}
}
