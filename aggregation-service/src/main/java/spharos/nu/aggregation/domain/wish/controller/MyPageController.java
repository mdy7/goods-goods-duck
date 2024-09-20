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
import spharos.nu.aggregation.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/aggregation/users")
@Tag(name = "Wish", description = "aggregation-service에서 마이페이지 관심상품 관련 API document")
public class MyPageController {

	private final MyPageService myPageService;

	@GetMapping("/wish")
	@Operation(summary = "관심 상품 조회", description = "상품코드 pagenation")
	public ResponseEntity<ApiResponse<WishGoodsResponseDto>> getWishGoods(@RequestHeader(value = "User-Uuid", required = false) String uuid,
		@RequestParam(value = "page", defaultValue = "0") Integer index) {

		return ApiResponse.success(myPageService.wishGoodsGet(uuid, index), "관심 상품 코드 조회 성공");
	}
}
