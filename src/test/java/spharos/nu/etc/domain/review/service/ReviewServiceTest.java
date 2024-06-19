package spharos.nu.etc.domain.review.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import spharos.nu.etc.domain.review.dto.request.ReviewRequestDto;
import spharos.nu.etc.domain.review.dto.response.ReviewListDto;
import spharos.nu.etc.domain.review.dto.response.ReviewResponseDto;
import spharos.nu.etc.domain.review.entity.Review;
import spharos.nu.etc.domain.review.repository.ReviewRepository;
import spharos.nu.etc.global.exception.CustomException;
import spharos.nu.etc.global.exception.errorcode.ErrorCode;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

	@Mock
	private ReviewRepository reviewRepository;

	@InjectMocks
	private ReviewService reviewService;

	private String sellerUuid;
	private Integer index;
	private Pageable pageable;
	private Page<ReviewListDto> reviewListPage;

	@Test
	@DisplayName("후기 조회 서비스 테스트")
	void testReviewsGet() {

		// given
		List<ReviewListDto> reviews = Arrays.asList(
			ReviewListDto.builder()
				.goodsCode("20240610")
				.content("good")
				.build(),
			ReviewListDto.builder()
				.goodsCode("20240611")
				.content("great")
				.build()
		);

		sellerUuid = "receiver_uuid1";
		index = 0;
		pageable = PageRequest.of(index, 10);
		reviewListPage = new PageImpl<>(reviews, pageable, reviews.size());

		when(reviewRepository.findByReceiverUuidOrderByCreatedAtDesc(eq(sellerUuid), any(Pageable.class))).thenReturn(
			reviewListPage);

		// when
		ReviewResponseDto res = reviewService.reviewsGet(sellerUuid, pageable);

		// then
		Assertions.assertThat(res).isNotNull();
		Assertions.assertThat(res.getNowPage()).isEqualTo(reviewListPage.getNumber());
		Assertions.assertThat(res.getMaxPage()).isEqualTo(reviewListPage.getTotalPages());
		Assertions.assertThat(res.getReviewList()).isEqualTo(reviewListPage.getContent());
	}

	@Test
	@DisplayName("이미 후기 존재 하는 후기 작성 테스트")
	public void testReviewCreate_ThrowsCustomException_WhenReviewAlreadyExists() {
		String writerUuid = "writer-uuid";
		String bidderUuid = "writer-uuid";
		String sellerUuid = "receiver-uuid";
		String goodsCode = "goods-code";
		Integer score = 50;
		String content = "good";
		ReviewRequestDto reviewRequestDto = new ReviewRequestDto(bidderUuid, sellerUuid, goodsCode, score, content);

		OngoingStubbing<Optional<Review>> optionalOngoingStubbing = when(
			reviewRepository.findByWriterUuidAndGoodsCode(writerUuid, goodsCode))
			.thenReturn(Optional.of(new Review()));

		CustomException thrownException = assertThrows(CustomException.class, () -> {
			reviewService.reviewCreate(bidderUuid, reviewRequestDto);
		});

		assertEquals(ErrorCode.ALREADY_REVIEW_CREATE, thrownException.getErrorCode());
		verify(reviewRepository, never()).save(any(Review.class));
	}

	@Test
	@DisplayName("후기 작성 테스트")
	public void testReviewCreate_Success() {
		String writerUuid = "writer-uuid";
		String bidderUuid = "writer-uuid";
		String sellerUuid = "receiver-uuid";
		String goodsCode = "goods-code";
		Integer score = 50;
		String content = "good";
		ReviewRequestDto reviewRequestDto = new ReviewRequestDto(bidderUuid, sellerUuid, goodsCode, score, content);

		when(reviewRepository.findByWriterUuidAndGoodsCode(writerUuid, goodsCode))
			.thenReturn(Optional.empty());

		reviewService.reviewCreate(bidderUuid, reviewRequestDto);

		verify(reviewRepository, times(1)).save(any(Review.class));
	}
}