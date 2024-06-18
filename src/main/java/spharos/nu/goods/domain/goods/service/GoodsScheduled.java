package spharos.nu.goods.domain.goods.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spharos.nu.goods.domain.bid.repository.BidRepository;
import spharos.nu.goods.domain.goods.dto.event.GoodsStatusEventDto;
import spharos.nu.goods.domain.goods.dto.event.NotificationEventDto;
import spharos.nu.goods.domain.goods.entity.Goods;
import spharos.nu.goods.domain.goods.kafka.GoodsKafkaProducer;
import spharos.nu.goods.domain.goods.repository.GoodsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoodsScheduled {

    private final GoodsRepository goodsRepository;
    private final BidRepository bidRepository;
    private final GoodsKafkaProducer kafkaProducer;
    private final KafkaTemplate<String, NotificationEventDto> notificationEventDtoKafkaTemplate;

    @Scheduled(cron = "0 0/1 * * * *")
    @Transactional
    public void closeGoods() {
        List<Goods> closeGoods = goodsRepository.findByTradingStatusAndClosedAtBefore((byte) 1, LocalDateTime.now());

        for (Goods goods : closeGoods) {
            log.info("경매 종료된 상품: {}", goods.getGoodsCode());

            Goods updatedGoods = goodsRepository.save(Goods.builder()
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
                    .tradingStatus((byte)2)  // 경매마감
                    .isDisable(goods.getIsDisable())
                    .build());

            kafkaProducer.sendGoodsStatusEvent(GoodsStatusEventDto.builder()
                .goodsCode(updatedGoods.getGoodsCode())
                .tradingStatus(updatedGoods.getTradingStatus())
                .build());

            log.info("(상품 코드: {}) 거래상태 2로 변경 이벤트 발행",updatedGoods.getGoodsCode());

            List<String> uuid = bidRepository.findDistinctBiddersByGoodsCode(goods.getGoodsCode());
            uuid.add(goods.getSellerUuid());

            sendPushAlarm(goods, uuid);
        }
    }

    private void sendPushAlarm(Goods goods, List<String> uuid) {
        NotificationEventDto notificationEventDto = NotificationEventDto.builder()
                .uuid(uuid)
                .title(goods.getName() + " 상품의 경매가 종료되었습니다")
                .content("확인하세요.")
                .link("/goods/" + goods.getGoodsCode())
                .build();

        notificationEventDtoKafkaTemplate.send("notification-topic", notificationEventDto);
    }



    @Scheduled(cron = "0 0/1 * * * *")
    @Transactional
    public void openGoods() {
        List<Goods> startGoods = goodsRepository.findByTradingStatusAndOpenedAtBefore((byte) 0, LocalDateTime.now());

        for (Goods goods : startGoods) {
            log.info("경매 시작상품: {}", goods.getGoodsCode());

            Goods updatedGoods = goodsRepository.save(Goods.builder()
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
                    .tradingStatus((byte)1)  // 경매중
                    .isDisable(goods.getIsDisable())
                    .build());

            kafkaProducer.sendGoodsStatusEvent(GoodsStatusEventDto.builder()
                .goodsCode(updatedGoods.getGoodsCode())
                .tradingStatus(updatedGoods.getTradingStatus())
                .build()
            );

            log.info("(상품 코드: {}) 거래상태 1로 변경 이벤트 발행",updatedGoods.getGoodsCode());
        }
    }
}
