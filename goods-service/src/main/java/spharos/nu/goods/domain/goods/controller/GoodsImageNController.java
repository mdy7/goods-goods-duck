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
import spharos.nu.goods.domain.goods.service.GoodsImageService;
import spharos.nu.goods.domain.goods.service.GoodsService;
import spharos.nu.goods.global.apiresponse.ApiResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/goods-n")
@Tag(name = "GoodsImageN", description = "토큰 검증이 필요없는 굿즈 이미지 조회용 컨트롤러")
public class GoodsImageNController {
	private final GoodsImageService goodsImageService;

	@Operation(summary = "굿즈 썸네일 조회", description = "굿즈의 썸네일를 조회합니다")
	@GetMapping("/{goodsCode}/thumbnail")
	public ResponseEntity<ApiResponse<String>> getGoodsThumbnail(
		@PathVariable("goodsCode") String goodsCode
	) {
		return ApiResponse.success(goodsImageService.getGoodsThumbnail(goodsCode), "굿즈 썸네일 조회 성공");
	}

	@Operation(summary = "굿즈 이미지리스트 조회", description = "굿즈의 이미지리스트를 조회합니다")
	@GetMapping("/{goodsCode}/images")
	public ResponseEntity<ApiResponse<List<ImageDto>>> getGoodsImages(
		@PathVariable("goodsCode") String goodsCode
	) {
		return ApiResponse.success(goodsImageService.getGoodsImages(goodsCode), "굿즈 이미지리스트 조회 성공");
	}

}
