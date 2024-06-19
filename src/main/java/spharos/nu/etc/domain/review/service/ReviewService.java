package spharos.nu.etc.domain.review.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.review.dto.event.MemberReviewEventDto;
import spharos.nu.etc.domain.review.dto.event.NotificationEventDto;
import spharos.nu.etc.domain.review.dto.event.TradingCompleteEventDto;
import spharos.nu.etc.domain.review.dto.request.ReviewRequestDto;
import spharos.nu.etc.domain.review.dto.response.ReviewListDto;
import spharos.nu.etc.domain.review.dto.response.ReviewOneResponseDto;
import spharos.nu.etc.domain.review.dto.response.ReviewResponseDto;
import spharos.nu.etc.domain.review.entity.Review;
import spharos.nu.etc.domain.review.kafka.ReviewKafkaProducer;
import spharos.nu.etc.domain.review.repository.ReviewRepository;
import spharos.nu.etc.global.exception.CustomException;
import spharos.nu.etc.global.exception.errorcode.ErrorCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final ReviewKafkaProducer reviewKafkaProducer;

	/**
	 * 후기 1개 조회
	 *
	 * @param reviewId
	 */
	public ReviewOneResponseDto oneReviewGet(Long reviewId) {

		Review review = reviewRepository.findById(reviewId).orElseThrow();

		int score = review.getScore();
		int level;

		if (score >= 80) {
			level = 5;
		} else if (score >= 60) {
			level = 4;
		} else if (score >= 40) {
			level = 3;
		} else if (score >= 20) {
			level = 2;
		} else {
			level = 1;
		}

		return ReviewOneResponseDto.builder()
			.level(level)
			.writerUuid(review.getWriterUuid())
			.goodsCode(review.getGoodsCode())
			.content(review.getContent())
			.build();
	}

	/**
	 * 받은 후기 전체 조회
	 *
	 * @param receiverUuid
	 * @param pageable
	 */
	public ReviewResponseDto reviewsGet(String receiverUuid, Pageable pageable) {

		Page<Review> reviewPage = reviewRepository.findByReceiverUuidOrderByCreatedAtDesc(receiverUuid,
			pageable);

		List<ReviewListDto> reviewList = reviewPage.getContent().stream()
			.map(review -> ReviewListDto.builder()
				.reviewId(review.getId())
				.goodsCode(review.getGoodsCode())
				.content(review.getContent())
				.build())
			.toList();

		return ReviewResponseDto.builder()
			.totalCount(reviewPage.getTotalElements())
			.nowPage(reviewPage.getNumber())
			.maxPage(reviewPage.getTotalPages())
			.isLast(reviewPage.isLast())
			.reviewList(reviewList)
			.build();
	}

	/**
	 * 후기 작성
	 *
	 * @param writerUuid
	 * @param reviewRequestDto 작성 완료 후 member, goods, notification 서비스와 카프카 통신
	 */
	public Void reviewCreate(String writerUuid, ReviewRequestDto reviewRequestDto) {

		// 작성자와 수신자 구분
		String receiverUuid;

		if (writerUuid.equals(reviewRequestDto.getBidderUuid())) {
			receiverUuid = reviewRequestDto.getSellerUuid();
		} else {
			receiverUuid = reviewRequestDto.getBidderUuid();
		}

		String goodsCode = reviewRequestDto.getGoodsCode();
		Integer score = reviewRequestDto.getScore();

		// 이미 작성한 후기 처리
		reviewRepository.findByWriterUuidAndGoodsCode(writerUuid, goodsCode)
			.ifPresent(review -> {
				throw new CustomException(ErrorCode.ALREADY_REVIEW_CREATE);
			});

		// 후기 저장
		Review review = reviewRepository.save(Review.builder()
			.writerUuid(writerUuid)
			.receiverUuid(receiverUuid)
			.goodsCode(goodsCode)
			.score(score)
			.content(reviewRequestDto.getContent())
			.build());

		// 점수 반영 카프카 통신
		MemberReviewEventDto memberReviewEventDto = MemberReviewEventDto.builder()
			.reviewId(review.getId())
			.receiverUuid(receiverUuid)
			.score(score)
			.build();

		reviewKafkaProducer.sendMemberScore(memberReviewEventDto);

		// 후기 알림 카프카 통신
		NotificationEventDto notificationEventDto = NotificationEventDto.builder()
			.title("거래 후기 도착")
			.content("당신의 매너덕을 확인하세요.")
			.uuid(receiverUuid)
			.reviewId(review.getId())
			.link((byte)2)
			.build();
		reviewKafkaProducer.sendReviewNotification(notificationEventDto);

		// 개발 확인용 로그
		log.info("(수신자: {}) 수신완료 ", receiverUuid);
		log.info("(점수: {}) 점수확인 ", score);

		// 판매자, 입찰자 모두 후기 작성 완료시 상태 거래 완료로 바꾸는 카프카 통신
		// 변수 값 확인
		log.info("Receiver UUID: {}, Goods Code: {}", receiverUuid, goodsCode);

		if (reviewRepository.findByWriterUuidAndGoodsCode(receiverUuid, goodsCode).isPresent()) {

			TradingCompleteEventDto tradingCompleteEventDto = TradingCompleteEventDto.builder()
				.goodsCode(goodsCode)
				.build();
			reviewKafkaProducer.sendTradingStatus(tradingCompleteEventDto);

			// 개발 확인용 로그
			log.info("(상품 코드: {}) 경매 완료 ", goodsCode);
		}

		return null;
	}
}
