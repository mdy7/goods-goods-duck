package spharos.nu.goods.domain.mypage.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.goods.domain.bid.dto.response.BidGoodsCodeDto;
import spharos.nu.goods.domain.bid.repository.BidRepository;
import spharos.nu.goods.domain.bid.repository.WinningBidRepository;
import spharos.nu.goods.domain.mypage.dto.response.BidGoodsResponseDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

	private final BidRepository bidRepository;
	private final WinningBidRepository winningBidRepository;

	// 입찰한 상품 조회
	public BidGoodsResponseDto bidGoodsGet(String bidderUuid, Pageable pageable, byte status) {

		Page<BidGoodsCodeDto> biddingPage = bidRepository.findAllGoods(bidderUuid, pageable, status);

		return BidGoodsResponseDto.builder()
			.totalCount(biddingPage.getTotalElements())
			.nowPage(biddingPage.getNumber())
			.maxPage(biddingPage.getTotalPages())
			.isLast(biddingPage.isLast())
			.goodsList(biddingPage.getContent())
			.build();
	}

	public BidGoodsResponseDto winningBidGoodsGet(String bidderUuid, Pageable pageable, byte status) {

		Page<BidGoodsCodeDto> winningPage = winningBidRepository.findAllGoods(bidderUuid, pageable, status);

		return BidGoodsResponseDto.builder()
			.totalCount(winningPage.getTotalElements())
			.nowPage(winningPage.getNumber())
			.maxPage(winningPage.getTotalPages())
			.isLast(winningPage.isLast())
			.goodsList(winningPage.getContent())
			.build();
	}

}
