package spharos.nu.goods.domain.goods.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import spharos.nu.goods.domain.goods.dto.GoodsAllListDto;
import spharos.nu.goods.domain.goods.dto.GoodsCreateDto;
import spharos.nu.goods.domain.goods.dto.GoodsReadDto;
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
	public ResponseEntity<ApiResponse<GoodsReadDto>> getGoodsDetail(
		@PathVariable("goodsCode") String goodsCode
	) {
		return ApiResponse.success(goodsService.goodsRead(goodsCode), "굿즈 상세조회 성공");
	}

}
