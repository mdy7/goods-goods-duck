package spharos.nu.etc.domain.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.review.dto.event.MemberScoreEventDto;
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

	public ReviewResponseDto reviewsGet(String receiverUuid, Integer index) {

		Pageable pageable = PageRequest.of(index, 10);
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
		Integer score = reviewRequestDto.getScore();

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
			.score(score)
			.content(reviewRequestDto.getContent())
			.build());

		// 점수 반영 카프카 통신
		MemberScoreEventDto memberScoreEventDto = MemberScoreEventDto.builder()
			.receiverUuid(receiverUuid)
			.score(score)
			.build();

		kafkaProducer.sendMemberScore(memberScoreEventDto);

		// 개발 확인용 로그
		log.info("(수신자: {}) 수신완료 ", receiverUuid);
		log.info("(점수: {}) 점수확인 ", score);

		// 판매자, 입찰자 모두 후기 작성 완료시 상태 거래 완료로 바꾸는 카프카 통신

		return null;
	}
}
