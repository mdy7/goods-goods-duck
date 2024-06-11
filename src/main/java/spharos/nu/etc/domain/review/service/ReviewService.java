package spharos.nu.etc.domain.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.review.dto.request.ReviewRequestDto;
import spharos.nu.etc.domain.review.dto.response.ReviewListDto;
import spharos.nu.etc.domain.review.dto.response.ReviewResponseDto;
import spharos.nu.etc.domain.review.entity.Review;
import spharos.nu.etc.domain.review.repository.ReviewRepository;
import spharos.nu.etc.global.exception.CustomException;
import spharos.nu.etc.global.exception.errorcode.ErrorCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

	private final ReviewRepository reviewRepository;

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

		return null;
	}
}
