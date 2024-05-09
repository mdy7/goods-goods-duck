package spharos.nu.goods.domain.goods.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import spharos.nu.goods.domain.goods.entity.Goods;
import spharos.nu.goods.domain.goods.service.GoodsService;
import spharos.nu.goods.global.apiresponse.ApiResponse;

@RequiredArgsConstructor
@RestController
@Tag(name = "Goods", description = "테스트용 컨트롤러")
public class GoodsController {
	private final GoodsService goodsService;

	@Operation(summary = "전체 굿즈 조회", description = "테스트용으로 전체 굿즈를 조회합니다")
	@GetMapping()
	public ResponseEntity<ApiResponse<List<Goods>>> getAllGoods() {
		return ApiResponse.success(goodsService.getAllGoods(),"전체 조회 성공");
	}
}
