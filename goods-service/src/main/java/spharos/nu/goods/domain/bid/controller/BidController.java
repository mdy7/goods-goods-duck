package spharos.nu.goods.domain.bid.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.goods.domain.bid.dto.request.BidAddRequestDto;
import spharos.nu.goods.domain.bid.dto.response.BidListResponseDto;
import spharos.nu.goods.domain.bid.dto.response.BidTopPriceResponseDto;
import spharos.nu.goods.domain.bid.service.BidService;
import spharos.nu.goods.global.apiresponse.ApiResponse;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Bid", description = "입찰 API")
@RequestMapping("/api/v1/goods")
public class BidController {

    private final BidService bidService;

    @PostMapping("/{goodsCode}/bids")
    @Operation(summary = "입찰 등록", description = "해당 상품에 입찰을 등록함")
    public ResponseEntity<ApiResponse<Void>> biddingAdd(@PathVariable("goodsCode") String goodsCode,
                                                        @RequestBody BidAddRequestDto bidAddRequestDto,
                                                        @RequestHeader(value = "User-Uuid", required = false) String uuid) {
        bidService.addBid(goodsCode, bidAddRequestDto, uuid);
        return ApiResponse.success(null, "입찰 등록 성공");
    }

    @GetMapping("/{goodsCode}/bids/max")
    @Operation(summary = "상품별 최고가 조회", description = "해당 상품의 최고가를 조회함")
    public ResponseEntity<ApiResponse<BidTopPriceResponseDto>> biddingMax(@PathVariable("goodsCode") String goodsCode,
                                                                          @RequestHeader(value = "User-Uuid", required = false) String uuid) {
        return ApiResponse.success(bidService.findMaximumPrice(goodsCode, uuid), "상품별 최고가 조회 성공");
    }

    @GetMapping("/{goodsCode}/bids")
    @Operation(summary = "상품별 입찰 조회", description = "상품별 입찰(리스트) 정보를 조회함")
    public ResponseEntity<ApiResponse<List<BidListResponseDto>>> biddingList(
            @PathVariable("goodsCode") String goodsCode,
            @RequestHeader(value = "User-Uuid", required = false) String uuid) {
        return ApiResponse.success(bidService.findBidList(goodsCode, uuid), "상품별 입찰 조회 성공");
    }

    @PostMapping("/{goodsCode}/bids/confirm")
    @Operation(summary = "낙찰 확정", description = "해당 상품의 입찰을 낙찰 확정함")
    public ResponseEntity<ApiResponse<Void>> biddingConfirm(@PathVariable("goodsCode") String goodsCode,
                                                            @RequestHeader(value = "User-Uuid", required = false) String uuid) {
        bidService.confirmWinningBid(goodsCode, uuid);
        return ApiResponse.success(null, "낙찰 확정 성공");
    }

    @PostMapping("/{goodsCode}/bids/cancel")
    @Operation(summary = "낙찰 취소", description = "해당 상품의 낙찰을 취소함")
    public ResponseEntity<ApiResponse<Void>> biddingCancel(@PathVariable("goodsCode") String goodsCode,
                                                           @RequestHeader(value = "User-Uuid", required = false) String uuid) {
        bidService.cancelWinningBid(goodsCode, uuid);
        return ApiResponse.success(null, "낙찰 취소 성공");
    }


}

