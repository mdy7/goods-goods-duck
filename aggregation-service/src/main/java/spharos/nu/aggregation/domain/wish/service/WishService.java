package spharos.nu.aggregation.domain.wish.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spharos.nu.aggregation.domain.aggregation.dto.event.CountEventDto;
import spharos.nu.aggregation.domain.aggregation.entity.BidCount;
import spharos.nu.aggregation.domain.aggregation.entity.WishCount;
import spharos.nu.aggregation.domain.aggregation.kafka.KafkaProducer;
import spharos.nu.aggregation.domain.aggregation.repository.WishCountRepository;
import spharos.nu.aggregation.domain.wish.entity.Wish;
import spharos.nu.aggregation.domain.wish.repository.WishRepository;
import spharos.nu.aggregation.global.exception.CustomException;

import static spharos.nu.aggregation.global.exception.errorcode.ErrorCode.ALREADY_EXIST_LIKE;
import static spharos.nu.aggregation.global.exception.errorcode.ErrorCode.NOT_FOUND_ENTITY;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class WishService {

    private final WishRepository wishRepository;
    private final WishCountRepository wishCountRepository;
    private final KafkaProducer kafkaProducer;

    /**
     * 관심상품 추가
     */
    @Transactional
    public void addWish(String goodsCode, String uuid) {
        validateWishAlreadyExists(goodsCode, uuid);

        wishRepository.save(Wish.builder()
                .goodsCode(goodsCode)
                .uuid(uuid)
                .build());

        /* 상품의 좋아요수 찾기 */
        WishCount wishCount = wishCountRepository.findByGoodsCode(goodsCode).orElseGet(() ->
                wishCountRepository.save(WishCount.builder()
                        .goodsCode(goodsCode)
                        .count(0L)
                        .build()));

        /* 업데이트해서 저장 */
        WishCount updatedWishCount = wishCountRepository.save(WishCount.builder()
            .id(wishCount.getId())
            .goodsCode(wishCount.getGoodsCode())
            .count(wishCount.getCount() + 1) //숫자 업데이트
            .build());

        /* new 좋아요수를 리드서버로 전송 */
        kafkaProducer.sendWishCountEvent(CountEventDto.builder()
            .goodsCode(updatedWishCount.getGoodsCode())
            .count(updatedWishCount.getCount())
            .build());

        log.info("(goodsCode: {}) 좋아요수 업데이트 이벤트 발행",updatedWishCount.getGoodsCode());

    }

    private void validateWishAlreadyExists(String goodsCode, String uuid) {
        wishRepository.findByGoodsCodeAndUuid(goodsCode, uuid)
                .ifPresent(wish -> {
                    throw new CustomException(ALREADY_EXIST_LIKE);
                });
    }


    /**
     * 관심상품 삭제
     */
    @Transactional
    public void deleteWish(String goodsCode, String uuid) {
        Wish wish = wishRepository.findByGoodsCodeAndUuid(goodsCode, uuid)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ENTITY));

        WishCount wishCount = wishCountRepository.findByGoodsCode(goodsCode).orElseThrow();

        wishCount.decreaseWishCount();

        /* new 좋아요수를 리드서버로 전송 */
        kafkaProducer.sendWishCountEvent(CountEventDto.builder()
            .goodsCode(wishCount.getGoodsCode())
            .count(wishCount.getCount())
            .build());

        wishRepository.delete(wish);
    }


    /**
     * 관심상품 여부 확인
     */
    public boolean isWished(String goodsCode, String uuid) {
        return wishRepository.findByGoodsCodeAndUuid(goodsCode, uuid).isPresent();
    }

}
