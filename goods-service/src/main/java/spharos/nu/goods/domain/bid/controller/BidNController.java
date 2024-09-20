package spharos.nu.goods.domain.bid.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import spharos.nu.goods.domain.bid.service.BidService;


@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Bid", description = "입찰 API")
@RequestMapping("/api/v1/goods-n")
public class BidNController {

	private final BidService bidService;

}

