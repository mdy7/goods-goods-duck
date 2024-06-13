package spharos.nu.etc.domain.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.review.dto.event.TradingCompleteEventDto;
import spharos.nu.etc.domain.review.dto.request.ReviewRequestDto;
import spharos.nu.etc.domain.review.dto.response.ReviewListDto;
import spharos.nu.etc.domain.review.dto.response.ReviewResponseDto;
import spharos.nu.etc.domain.review.entity.Review;
import spharos.nu.etc.domain.review.kafka.KafkaProducer;
import spharos.nu.etc.domain.review.repository.ReviewRepository;
import spharos.nu.etc.global.exception.CustomException;
import spharos.nu.etc.global.exception.errorcode.ErrorCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final KafkaProducer kafkaProducer;

	public ReviewResponseDto reviewsGet(String receiverUuid, Pageable pageable) {

		Page<ReviewListDto> reviewPage = reviewRepository.findByReceiverUuidOrderByCreatedAtDesc(receiverUuid,
			pageable);

		return ReviewResponseDto.builder()
			.totalCount(reviewPage.getTotalElements())
			.nowPage(reviewPage.getNumber())
			.maxPage(reviewPage.getTotalPages())
			.isLast(reviewPage.isLast())
			.reviewList(reviewPage.getContent())
			.build();

	}

	public Void reviewCreate(String writerUuid, String receiverUuid, ReviewRequestDto reviewRequestDto) {

		String goodsCode = reviewRequestDto.getGoodsCode();

		// 이미 작성한 후기 처리
		reviewRepository.findByWriterUuidAndGoodsCode(writerUuid, goodsCode)
			.ifPresent(review -> {
				throw new CustomException(ErrorCode.ALREADY_REVIEW_CREATE);
			});

		// 후기 저장
		reviewRepository.save(Review.builder()
			.writerUuid(writerUuid)
			.receiverUuid(receiverUuid)
			.goodsCode(goodsCode)
			.score(reviewRequestDto.getScore())
			.content(reviewRequestDto.getContent())
			.build());

		// 점수 반영 로직 처리 후 카프카 통신

		// 판매자, 입찰자 모두 후기 작성 완료시 상태 거래 완료로 바꾸는 카프카 통신
		// 변수 값 확인
		log.info("Receiver UUID: {}, Goods Code: {}", receiverUuid, goodsCode);

		if (reviewRepository.findByWriterUuidAndGoodsCode(receiverUuid, goodsCode).isPresent()) {
			log.info("여기 오시나요?");
			TradingCompleteEventDto tradingCompleteEventDto = TradingCompleteEventDto.builder()
						.goodsCode(goodsCode)
						.build();
			kafkaProducer.sendTradingStatus(tradingCompleteEventDto);

			// 개발 확인용 로그
			log.info("(상품 코드: {}) 경매 완료 ", goodsCode);
		}

		return null;
	}
}
