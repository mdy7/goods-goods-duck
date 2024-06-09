package spharos.nu.aggregation.domain.wish.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import spharos.nu.aggregation.domain.wish.service.WishService;
import spharos.nu.aggregation.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/aggregation/wish")
@Tag(name = "Wish", description = "aggregation-service에서 관심상품 관련 API document")
public class WishController {

    private final WishService wishService;

    @PostMapping("/{goodsCode}")
    public ResponseEntity<ApiResponse<Void>> wishAdd(@RequestHeader("User-Uuid") String uuid, @PathVariable String goodsCode) {
        wishService.addWish(goodsCode, uuid);
        return ApiResponse.success(null, "관심상품 추가 성공");
    }

    @DeleteMapping("/{goodsCode}")
    public ResponseEntity<ApiResponse<Void>> wishDelete(@RequestHeader("User-Uuid") String uuid, @PathVariable String goodsCode) {
        wishService.deleteWish(goodsCode, uuid);
        return ApiResponse.success(null, "관심상품 삭제 성공");
    }

    @GetMapping("/{goodsCode}")
    public ResponseEntity<ApiResponse<Boolean>> isWished(@RequestHeader("User-Uuid") String uuid, @PathVariable String goodsCode) {
        return ApiResponse.success(wishService.isWished(goodsCode, uuid), "관심상품 여부 확인 성공");
    }

	@GetMapping("/{goodsCode}/is-wish")
	@Operation(summary = "관심(좋아요) 여부 조회", description = "상품코드로 좋아요 여부를 조회합니다")
	public ResponseEntity<ApiResponse<Boolean>> getIsWish(@RequestHeader(value = "User-Uuid", required = false) String uuid,
		@PathVariable(value = "goodsCode") String goodsCode) {

		return ApiResponse.success(wishService.getIsWish(uuid, goodsCode), "좋아요 여부 조회 성공");
	}
}
