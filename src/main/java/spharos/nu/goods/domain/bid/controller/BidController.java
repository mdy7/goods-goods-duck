package spharos.nu.goods.domain.bid.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.goods.domain.bid.dto.request.BidAddRequestDto;
import spharos.nu.goods.domain.bid.dto.response.BidListResponseDto;
import spharos.nu.goods.domain.bid.service.BidService;
import spharos.nu.goods.global.apiresponse.ApiResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Bid", description = "입찰 API")
@RequestMapping("/api/v1")
public class BidController {

	private final BidService bidService;

	@PostMapping("/bids/{goodsCode}")
	@Operation(summary = "입찰 등록", description = "해당 상품에 입찰을 등록함")
	public ResponseEntity<ApiResponse<Void>> biddingAdd(@PathVariable("goodsCode") String goodsCode,
		@RequestBody BidAddRequestDto bidAddRequestDto,
		@RequestHeader(value = "User-Uuid", required = false) String uuid) {
		// log.info("uuid : {}", uuid);
		bidService.addBid(goodsCode, bidAddRequestDto, uuid);
		return ApiResponse.success(null, "입찰 등록 성공");
	}

	@GetMapping("/bids-n/{goodsCode}")
	@Operation(summary = "상품별 입찰 조회", description = "상품별 입찰(리스트) 정보를 조회함")
	public ResponseEntity<ApiResponse<List<BidListResponseDto>>> biddingList(
		@PathVariable("goodsCode") String goodsCode) {
		return ApiResponse.success(bidService.findBidList(goodsCode), "상품별 입찰 조회 성공");
	}
}

