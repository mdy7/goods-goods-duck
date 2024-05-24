package spharos.nu.goods.domain.goods.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import spharos.nu.goods.global.apiresponse.ApiResponse;

import java.time.LocalDateTime;

@FeignClient(name = "bid-service")
public interface BidServiceClient {

    @PostMapping("/v1/bids-n/{goodsCode}/winning-bid")
    ResponseEntity<ApiResponse> selectWinningBid(@PathVariable String goodsCode, @RequestBody LocalDateTime closedAt);


}
