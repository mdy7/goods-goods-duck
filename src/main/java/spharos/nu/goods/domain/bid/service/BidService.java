package spharos.nu.goods.domain.bid.service;

import static spharos.nu.goods.global.exception.errorcode.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.goods.domain.bid.dto.request.BidAddRequestDto;
import spharos.nu.goods.domain.bid.dto.response.BidListResponseDto;
import spharos.nu.goods.domain.bid.dto.response.BidTopPriceResponseDto;
import spharos.nu.goods.domain.bid.entity.Bid;
import spharos.nu.goods.domain.bid.entity.WinningBid;
import spharos.nu.goods.domain.bid.repository.BidRepository;
import spharos.nu.goods.domain.bid.repository.WinningBidRepository;
import spharos.nu.goods.domain.goods.entity.Goods;
import spharos.nu.goods.domain.goods.repository.GoodsRepository;
import spharos.nu.goods.global.exception.CustomException;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BidService {

    private final BidRepository bidRepository;
    private final WinningBidRepository winningBidRepository;
    private final GoodsRepository goodsRepository;

    /**
     * 입찰 등록
     */
    @Transactional
    public void addBid(String goodsCode, BidAddRequestDto bidAddRequestDto, String bidderUuid) {
        Goods goods = goodsRepository.findOneByGoodsCode(goodsCode)
                .orElseThrow(() -> new CustomException(NOT_FOUND_GOODS));

        // 최소가격보다 낮은 가격으로 입찰 불가
        if (bidAddRequestDto.getPrice() <= goods.getMinPrice()) {
            throw new CustomException(TOO_LOW_PRICE);
        }

        // 경매 시작 전 입찰 불가
        if (goods.getOpenedAt().isAfter(LocalDateTime.now())) {
            throw new CustomException(BID_NOT_START);
        }

        // 경매 종료시간이 지난 경우 입찰 불가
        if (goods.getClosedAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(AFTER_BID_END);
        }

        // 판매자가 입찰 불가
        if (bidderUuid.equals(goods.getSellerUuid())) {
            throw new CustomException(SELLER_CANNOT_BID);
        }

        bidRepository.save(Bid.builder()
                .goodsCode(goodsCode)
                .bidderUuid(bidderUuid)
                .price(bidAddRequestDto.getPrice())
                .build());
    }


    /**
     * 상품별 입찰 조회
     */
    public List<BidListResponseDto> findBidList(String goodsCode, String uuid) {
        validateSeller(goodsCode, uuid);

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

    private Goods validateSeller(String goodsCode, String uuid) {
        Goods goods = goodsRepository.findOneByGoodsCode(goodsCode)
                .orElseThrow(() -> new CustomException(NOT_FOUND_GOODS));

        // 본인이 판매자가 아닌경우
        if (!goods.getSellerUuid().equals(uuid)) {
            throw new CustomException(IS_NOT_SELLER);
        }

        return goods;
    }


    /**
     * 최고가 조회
     */
    public BidTopPriceResponseDto findMaximumPrice(String goodsCode, String uuid) {
        validateSeller(goodsCode, uuid);

        Bid bid = bidRepository.findFirstByGoodsCodeOrderByPriceDescCreatedAtAsc(goodsCode)
                .orElseThrow(() -> new CustomException(NO_BIDDER));

        return BidTopPriceResponseDto.builder()
                .bidId(bid.getId())
                .bidderUuid(bid.getBidderUuid())
                .price(bid.getPrice())
                .build();
    }

    /**
     * 낙찰 확정
     */
    @Transactional
    public void confirmWinningBid(String goodsCode, String uuid) {
        Goods goods = validateSeller(goodsCode, uuid);

        if (goods.getTradingStatus() != 2) {
            throw new CustomException(INVALID_TRADING_STATUS);
        }

        winningBidRepository.findByGoodsCode(goodsCode)
                .ifPresent(winningBid -> {
                    throw new CustomException(WINNING_BID_ALREADY_EXIST);
                });

        // 낙찰자 조회: 가장 높은금액 가장 먼저 입찰한 사람
        Bid bid = bidRepository.findFirstByGoodsCodeOrderByPriceDescCreatedAtAsc(goodsCode)
                .orElseThrow(() -> new CustomException(NO_BIDDER));

        winningBidRepository.save(WinningBid.builder()
                .goodsCode(goodsCode)
                .bidderUuid(bid.getBidderUuid())
                .sellerUuid(goods.getSellerUuid())
                .winningPrice(bid.getPrice())
                .build()
        );

        goodsRepository.save(Goods.builder()
                .id(goods.getId())
                .categoryId(goods.getCategoryId())
                .sellerUuid(goods.getSellerUuid())
                .goodsCode(goods.getGoodsCode())
                .name(goods.getName())
                .minPrice(goods.getMinPrice())
                .description(goods.getDescription())
                .openedAt(goods.getOpenedAt())
                .closedAt(goods.getClosedAt())
                .wishTradeType(goods.getWishTradeType())
                .tradingStatus((byte) 3)
                .isDisable(goods.getIsDisable())
                .build()
        );
    }

    /**
     * 낙찰 취소
     */
    @Transactional
    public void cancelWinningBid(String goodsCode, String uuid) {
        validateSeller(goodsCode, uuid);

        Goods goods = goodsRepository.findOneByGoodsCode(goodsCode)
                .orElseThrow(() -> new CustomException(NOT_FOUND_GOODS));

        if (goods.getTradingStatus() != 2) {
            throw new CustomException(INVALID_TRADING_STATUS);
        }

        goodsRepository.save(
                Goods.builder()
                        .id(goods.getId())
                        .name(goods.getName())
                        .description(goods.getDescription())
                        .categoryId(goods.getCategoryId())
                        .minPrice(goods.getMinPrice())
                        .openedAt(goods.getOpenedAt())
                        .wishTradeType(goods.getWishTradeType())
                        .sellerUuid(uuid)
                        .goodsCode(goods.getGoodsCode())
                        .closedAt(goods.getClosedAt())
                        .tradingStatus((byte) 5)
                        .isDisable(goods.getIsDisable())
                        .build()
        );
    }


}



