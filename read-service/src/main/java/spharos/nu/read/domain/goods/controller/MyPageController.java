package spharos.nu.read.domain.goods.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import spharos.nu.read.domain.goods.dto.response.GoodsSellResponseDto;
import spharos.nu.read.domain.goods.service.MyPageService;
import spharos.nu.read.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/read")
@Tag(name = "MyPage", description = "토큰을 검증하는 읽기용 마이페이지 컨트롤러")
public class MyPageController {

	private final MyPageService myPageService;

	@GetMapping("/sell")
	@Operation(summary = "회원이 등록한 상품 조회", description = "상품코드, 상품썸네일, 상품명, 시작가격, 상태 데이터")
	public ResponseEntity<ApiResponse<GoodsSellResponseDto>> getSellGoods(
		@RequestHeader(value = "User-Uuid", required = false) String uuid,
		@PageableDefault(size = 10, page = 0) Pageable pageable,
		@RequestParam(value = "status", required = false) Byte status) {

		return ApiResponse.success(myPageService.sellGoodsGet(uuid, pageable, status), "등록한 상품 조회 성공");
	}
}
