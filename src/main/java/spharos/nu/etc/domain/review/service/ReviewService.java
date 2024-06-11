package spharos.nu.etc.domain.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.review.dto.response.ReviewListDto;
import spharos.nu.etc.domain.review.dto.response.ReviewResponseDto;
import spharos.nu.etc.domain.review.entity.Review;
import spharos.nu.etc.domain.review.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

	private final ReviewRepository reviewRepository;

	public ReviewResponseDto reviewsGet(String receiverUuid, Integer index) {

		Pageable pageable = PageRequest.of(index, 10);
		Page<ReviewListDto> reviewPage = reviewRepository.findByReceiverUuidOrderByCreatedAtDesc(receiverUuid, pageable);

		return ReviewResponseDto.builder()
			.totalCount(reviewPage.getTotalElements())
			.nowPage(reviewPage.getNumber())
			.maxPage(reviewPage.getTotalPages())
			.isLast(reviewPage.isLast())
			.reviewList(reviewPage.getContent())
			.build();

	}
}
