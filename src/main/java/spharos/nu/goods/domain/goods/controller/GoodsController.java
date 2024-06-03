package spharos.nu.goods.domain.goods.controller;

import java.util.List;

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
import spharos.nu.goods.domain.goods.dto.GoodsCodeDto;
import spharos.nu.goods.domain.goods.entity.Goods;
import spharos.nu.goods.domain.goods.service.GoodsService;
import spharos.nu.goods.global.apiresponse.ApiResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/goods")
@Tag(name = "Goods", description = "토큰을 검증하는 굿즈 컨트롤러")
public class GoodsController {
	private final GoodsService goodsService;

	@Operation(summary = "굿즈 등록", description = "판매할 굿즈를 등록(생성)합니다")
	@PostMapping()
	public ResponseEntity<ApiResponse<String>> createGoods(
		@RequestHeader(value = "User-Uuid", required = false) String uuid,
		@RequestBody GoodsCreateDto goodsCreateDto
	) {
		return ApiResponse.success(goodsService.goodsCreate(uuid, goodsCreateDto), "굿즈 등록 성공");
	}

	/*
	굿즈 입찰 전 완전히 삭제
	*/
	@Operation(summary = "굿즈 Hard Delete", description = "굿즈를 삭제합니다")
	@DeleteMapping("/{goodsCode}")
	public ResponseEntity<ApiResponse<Void>> goodsDelete(
		@RequestHeader(value = "User-Uuid", required = false) String uuid,
		@PathVariable("goodsCode") String goodsCode
	) {
		return ApiResponse.success(goodsService.goodsDelete(goodsCode), "굿즈 Hard Delete 성공");
	}

	/*
	입찰 시작되면 disable 처리
	 */
	@Operation(summary = "굿즈 Soft Delete", description = "굿즈를 삭제합니다")
	@DeleteMapping("/{goodsCode}/disable")
	public ResponseEntity<ApiResponse<Void>> goodsDisable(
		@RequestHeader(value = "User-Uuid", required = false) String uuid,
		@PathVariable("goodsCode") String goodsCode
	) {
		return ApiResponse.success(goodsService.goodsDisable(uuid, goodsCode), "굿즈 Soft Delete 성공");
	}

}
