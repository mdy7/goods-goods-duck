package spharos.nu.etc.domain.review.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import spharos.nu.etc.domain.review.dto.response.ReviewListDto;
import spharos.nu.etc.domain.review.dto.response.ReviewResponseDto;
import spharos.nu.etc.domain.review.repository.ReviewRepository;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

	@Mock
	private ReviewRepository reviewRepository;

	@InjectMocks
	private ReviewService reviewService;

	private String receiverUuid;
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

		receiverUuid = "receiver_uuid1";
		index = 0;
		pageable = PageRequest.of(index, 10);
		reviewListPage = new PageImpl<>(reviews, pageable, reviews.size());

		when(reviewRepository.findByReceiverUuidOrderByCreatedAtDesc(eq(receiverUuid), any(Pageable.class))).thenReturn(
			reviewListPage);

		// when
		ReviewResponseDto res = reviewService.reviewsGet(receiverUuid, index);

		// then
		Assertions.assertThat(res).isNotNull();
		Assertions.assertThat(res.getNowPage()).isEqualTo(reviewListPage.getNumber());
		Assertions.assertThat(res.getMaxPage()).isEqualTo(reviewListPage.getTotalPages());
		Assertions.assertThat(res.getReviewList()).isEqualTo(reviewListPage.getContent());
	}
}