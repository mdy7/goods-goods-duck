package spharos.nu.goods.domain.goods.controller;

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
import spharos.nu.goods.domain.goods.dto.GoodsSellResponseDto;
import spharos.nu.goods.domain.goods.dto.GoodsWishResponseDto;
import spharos.nu.goods.domain.goods.service.MyPageService;
import spharos.nu.goods.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/goods/users")
@Tag(name = "MyPage", description = "goods-service에서 마이페이지 관련 API document")
public class MyPageController {

	private final MyPageService myPageService;

	// 등록한 상품 조회
	@GetMapping("/sell")
	@Operation(summary = "회원이 등록한 상품 조회", description = "상품코드, 상품썸네일, 상품명, 시작가격, 상태 데이터")
	public ResponseEntity<ApiResponse<GoodsSellResponseDto>> getSellGoods(@RequestHeader(value = "User-Uuid", required = false) String uuid,
		@RequestParam(value = "page", defaultValue = "0") Integer index,
		@RequestParam(value = "status", required = false) byte statusNum) {

		return ApiResponse.success(myPageService.sellGoodsGet(uuid, index, statusNum), "등록한 상품 조회 성공");
	}

}
