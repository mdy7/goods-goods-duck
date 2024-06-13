package spharos.nu.goods.domain.bid.service;

import static spharos.nu.goods.global.exception.errorcode.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.goods.domain.bid.dto.event.WinningEventDto;
import spharos.nu.goods.domain.bid.dto.request.BidAddRequestDto;
import spharos.nu.goods.domain.bid.dto.response.BidListResponseDto;
import spharos.nu.goods.domain.bid.entity.Bid;
import spharos.nu.goods.domain.bid.entity.WinningBid;
import spharos.nu.goods.domain.bid.kafka.KafkaProducer;
import spharos.nu.goods.domain.bid.repository.BidRepository;
import spharos.nu.goods.domain.bid.repository.WinningBidRepository;
import spharos.nu.goods.domain.goods.entity.Goods;
import spharos.nu.goods.global.exception.CustomException;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BidService {

	private final BidRepository bidRepository;
	private final WinningBidRepository winningBidRepository;
	private final KafkaProducer kafkaProducer;

	/*
	 * 입찰 등록
	 */
	@Transactional
	public void addBid(String goodsCode, BidAddRequestDto bidAddRequestDto, String bidderUuid) {
		bidRepository.save(Bid.builder()
			.goodsCode(goodsCode)
			.bidderUuid(bidderUuid)
			.price(bidAddRequestDto.getPrice())
			.build());
	}

	/*
	 * 상품별 입찰 조회
	 */
	public List<BidListResponseDto> findBidList(String goodsCode) {
		List<Bid> bids = bidRepository.findByGoodsCode(goodsCode);

		return bids.stream()
			.map(bidding -> BidListResponseDto.builder()
				.bidId(bidding.getId())
				.bidderUuid(bidding.getBidderUuid())
				.price(bidding.getPrice())
				.createdAt(bidding.getCreatedAt())
				.build())
			.toList();
	}

	/*
	 * 낙찰자 선정
	 * 기준 : 가장 높은 가격, 가장 빠른 시간
	 */
	@Transactional
	public void addWinningBid(Goods goods) {
		String goodsCode = goods.getGoodsCode();
		String sellerUuid = goods.getSellerUuid();
		LocalDateTime closedAt = goods.getClosedAt();

		log.info("낙찰자 선정 시작...");
		List<Bid> bid = bidRepository.findByGoodsCodeAndCreatedAtBeforeOrderByPriceDescCreatedAtAsc(
			goodsCode, closedAt);

		if (bid.isEmpty()) {
			log.info("(상품코드: {})에 대한 입찰자가 없습니다.",goodsCode);
			return;
		}

		winningBidRepository.findByGoodsCode(goodsCode)
			.ifPresent(winningBid -> {
				throw new CustomException(WINNING_BID_ALREADY_EXIST);
			});
		log.info("물건번호는 {}",goodsCode);
		log.info("파는사람 {}",sellerUuid);
		log.info("사는사람 {}",bid.get(0).getBidderUuid());
		log.info("낙찰가 {}",bid.get(0).getPrice());

		winningBidRepository.save(WinningBid.builder()
			.goodsCode(goodsCode)
			.bidderUuid(bid.get(0).getBidderUuid())
			.sellerUuid(sellerUuid)
			.winningPrice(bid.get(0).getPrice())
			.build());

		// 낙찰 topic에 낙찰 이벤트 발행
        log.info("(상품 코드: {}) 낙찰 완료 이벤트 발행", goodsCode);
        WinningEventDto winningEventDto = WinningEventDto.builder()
			.goodsCode(goodsCode)
			.bidderUuid(bid.get(0).getBidderUuid())
			.sellerUuid(sellerUuid)
			.build();

		kafkaProducer.sendWinningEvent(winningEventDto);

		log.info("(상품코드 :{}) 낙찰 완료",goodsCode);
	}

}
