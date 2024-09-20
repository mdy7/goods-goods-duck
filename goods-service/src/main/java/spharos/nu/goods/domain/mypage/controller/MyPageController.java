package spharos.nu.goods.domain.mypage.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import lombok.extern.slf4j.Slf4j;
import spharos.nu.goods.domain.mypage.dto.response.BidGoodsResponseDto;
import spharos.nu.goods.domain.mypage.service.MyPageService;
import spharos.nu.goods.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/goods")
@Tag(name = "MyPage", description = "goods-service에서 마이페이지 관련 API document")
public class MyPageController {

	private final MyPageService myPageService;

	@GetMapping("/bids")
	@Operation(summary = "입찰한 상품 코드", description = "회원이 입찰한 상품 코드 리스트")
	public ResponseEntity<ApiResponse<BidGoodsResponseDto>> getBidGoods(
		@RequestHeader(value = "User-Uuid", required = false) String uuid,
		@PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
		@RequestParam(value = "status", defaultValue = "9") byte status) {

		return ApiResponse.success(myPageService.bidGoodsGet(uuid, pageable, status), "입찰한 상품 조회 성공");
	}

	@GetMapping("/winning-bid")
	@Operation(summary = "낙찰 받은 상품 코드", description = "낙찰 받은 상품 코드 리스트")
	public ResponseEntity<ApiResponse<BidGoodsResponseDto>> getWinningBidGoods(
		@RequestHeader(value = "User-Uuid", required = false) String uuid,
		@PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
		@RequestParam(value = "status", defaultValue = "9") byte status) {

		return ApiResponse.success(myPageService.winningBidGoodsGet(uuid, pageable, status), "낙찰 받은 상품 조회 성공");
	}

}
