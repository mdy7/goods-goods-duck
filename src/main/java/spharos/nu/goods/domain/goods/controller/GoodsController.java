package spharos.nu.goods.domain.goods.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import spharos.nu.goods.domain.goods.dto.request.GoodsCreateDto;
import spharos.nu.goods.domain.goods.service.GoodsService;
import spharos.nu.goods.global.apiresponse.ApiResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/goods")
@Slf4j
@Tag(name = "Goods", description = "토큰을 검증하는 굿즈 컨트롤러")
public class GoodsController {
	private final GoodsService goodsService;

	@Operation(summary = "굿즈 등록", description = "판매할 굿즈를 등록(생성)합니다")
	@PostMapping()
	public ResponseEntity<ApiResponse<String>> createGoods(
		@RequestHeader(value = "User-Uuid", required = false) String uuid,
		@RequestBody GoodsCreateDto goodsCreateDto
	) {
        log.info("컨트롤러단에서 확인"+goodsCreateDto.getImages());
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
		goodsService.goodsDelete(goodsCode);
		return ApiResponse.success(null, "굿즈 Hard Delete 성공");
	}

	/*
	입찰 시작되면 disable 처리
	 */
	@Operation(summary = "굿즈 Soft Delete", description = "굿즈를 숨김합니다")
	@DeleteMapping("/{goodsCode}/disable")
	public ResponseEntity<ApiResponse<Void>> goodsDisable(
		@RequestHeader(value = "User-Uuid", required = false) String uuid,
		@PathVariable("goodsCode") String goodsCode
	) {
		goodsService.goodsDisable(uuid, goodsCode);
		return ApiResponse.success(null, "굿즈 Soft Delete 성공");
	}

}
