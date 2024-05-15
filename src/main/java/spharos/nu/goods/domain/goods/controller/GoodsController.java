package spharos.nu.goods.domain.goods.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import spharos.nu.goods.domain.goods.dto.GoodsCreateDto;
import spharos.nu.goods.domain.goods.dto.GoodsReadDto;
import spharos.nu.goods.domain.goods.entity.Goods;
import spharos.nu.goods.domain.goods.service.GoodsService;
import spharos.nu.goods.global.apiresponse.ApiResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/goods")
@Tag(name = "Goods", description = "굿즈 컨트롤러")
public class GoodsController {
	private final GoodsService goodsService;

	@Operation(summary = "전체 굿즈 조회", description = "테스트용으로 전체 굿즈를 조회합니다")
	@GetMapping()
	public ResponseEntity<ApiResponse<List<Goods>>> getAllGoods() {
		return ApiResponse.success(goodsService.getAllGoods(),"전체 조회 성공");
	}

	@Operation(summary = "굿즈 등록", description = "판매할 굿즈를 등록(생성)합니다")
	@PostMapping()
	public ResponseEntity<ApiResponse<Long>> goodsCreate(
		@RequestBody GoodsCreateDto goodsCreateDto
	) {
		return ApiResponse.success(goodsService.goodsCreate(goodsCreateDto),"굿즈 등록 성공");
	}

	@Operation(summary = "굿즈 상세 조회", description = "굿즈의 상세정보를 조회합니다")
	@GetMapping("/{goodsCode}")
	public ResponseEntity<ApiResponse<GoodsReadDto>> goodsRead(
		@PathVariable("goodsCode") String code
	) {
		return ApiResponse.success(goodsService.goodsRead(code),"굿즈 상세조회 성공");
	}

}
