package spharos.nu.goods.domain.bid.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spharos.nu.goods.domain.bid.dto.request.BidAddRequestDto;
import spharos.nu.goods.domain.bid.dto.response.BidListResponseDto;
import spharos.nu.goods.domain.bid.service.BidService;
import spharos.nu.goods.global.apiresponse.ApiResponse;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Bid", description = "입찰 API")
@RequestMapping("/api/v1/goods-n")
public class BidNController {

	private final BidService bidService;

	@GetMapping("/bids/{goodsCode}")
	@Operation(summary = "상품별 입찰 조회", description = "상품별 입찰(리스트) 정보를 조회함")
	public ResponseEntity<ApiResponse<List<BidListResponseDto>>> biddingList(
		@PathVariable("goodsCode") String goodsCode) {
		return ApiResponse.success(bidService.findBidList(goodsCode), "상품별 입찰 조회 성공");
	}
}

