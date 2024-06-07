package spharos.nu.aggregation.domain.wish.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.aggregation.domain.wish.dto.WishGoodsResponseDto;
import spharos.nu.aggregation.domain.wish.service.MyPageService;
import spharos.nu.aggregation.domain.wish.service.WishService;
import spharos.nu.aggregation.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/aggregation")
@Tag(name = "Wish", description = "aggregation-service에서 마이페이지 관심상품 관련 API document")
public class WishController {

	private final WishService wishService;

	@GetMapping("/{goodsCode}/is-wish")
	@Operation(summary = "관심(좋아요) 여부 조회", description = "상품코드로 좋아요 여부를 조회합니다")
	public ResponseEntity<ApiResponse<Boolean>> getIsWish(@RequestHeader(value = "User-Uuid", required = false) String uuid,
		@RequestParam(value = "goodsCode") String goodsCode) {

		return ApiResponse.success(wishService.getIsWish(uuid, goodsCode), "좋아요 여부 조회 성공");
	}
}
