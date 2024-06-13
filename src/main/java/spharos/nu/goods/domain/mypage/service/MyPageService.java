package spharos.nu.goods.domain.mypage.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.goods.domain.mypage.dto.response.BidGoodsResponseDto;
import spharos.nu.goods.domain.bid.repository.BidRepository;
import spharos.nu.goods.domain.bid.repository.WinningBidRepository;
import spharos.nu.goods.domain.goods.repository.GoodsRepository;
import spharos.nu.goods.domain.goods.dto.response.GoodsCodeDto;
import spharos.nu.goods.domain.mypage.dto.response.GoodsSellResponseDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

	private final GoodsRepository goodsRepository;
	private final BidRepository bidRepository;
	private final WinningBidRepository winningBidRepository;

	public GoodsSellResponseDto sellGoodsGet(String uuid, Integer index, byte statusNum) {

		Pageable pageable = PageRequest.of(index, 10);
		Page<GoodsCodeDto> goodsCodePage = goodsRepository.findAllGoods(uuid, statusNum, pageable);

		return GoodsSellResponseDto.builder()
			.totalCount(goodsCodePage.getTotalElements())
			.nowPage(goodsCodePage.getNumber())
			.maxPage(goodsCodePage.getTotalPages())
			.isLast(goodsCodePage.isLast())
			.goodsList(goodsCodePage.getContent())
			.build();
	}

	public BidGoodsResponseDto bidGoodsGet(String bidderUuid, Integer index) {

		Pageable pageable = PageRequest.of(index, 10);
		Page<GoodsCodeDto> biddingPage = bidRepository.findByBidderUuidOrderByCreatedAtDesc(bidderUuid, pageable);

		return BidGoodsResponseDto.builder()
			.totalCount(biddingPage.getTotalElements())
			.nowPage(biddingPage.getNumber())
			.maxPage(biddingPage.getTotalPages())
			.isLast(biddingPage.isLast())
			.goodsList(biddingPage.getContent())
			.build();
	}

	public BidGoodsResponseDto winningBidGoodsGet(String bidderUuid, Integer index) {

		Pageable pageable = PageRequest.of(index, 10);
		Page<GoodsCodeDto> winningPage = winningBidRepository.findByBidderUuidOrderByCreatedAtDesc(bidderUuid,
			pageable);

		return BidGoodsResponseDto.builder()
			.totalCount(winningPage.getTotalElements())
			.nowPage(winningPage.getNumber())
			.maxPage(winningPage.getTotalPages())
			.isLast(winningPage.isLast())
			.goodsList(winningPage.getContent())
			.build();
	}

}
