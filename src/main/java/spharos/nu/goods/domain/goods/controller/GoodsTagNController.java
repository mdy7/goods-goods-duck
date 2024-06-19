package spharos.nu.goods.domain.goods.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import spharos.nu.goods.domain.goods.dto.request.ImageDto;
import spharos.nu.goods.domain.goods.dto.request.TagDto;
import spharos.nu.goods.domain.goods.service.GoodsService;
import spharos.nu.goods.domain.goods.service.GoodsTagService;
import spharos.nu.goods.global.apiresponse.ApiResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/goods-n")
@Tag(name = "GoodsTagN", description = "토큰 검증이 필요없는 굿즈 태그 조회용 컨트롤러")
public class GoodsTagNController {
	private final GoodsTagService goodsTagService;

	@Operation(summary = "굿즈 태그리스트 조회", description = "굿즈의 태그리스트를 조회합니다")
	@GetMapping("/{goodsCode}/tags")
	public ResponseEntity<ApiResponse<List<TagDto>>> getGoodsTags(
		@PathVariable("goodsCode") String goodsCode
	) {
		return ApiResponse.success(goodsTagService.getGoodsTags(goodsCode), "굿즈 태그리스트 조회 성공");
	}

}
