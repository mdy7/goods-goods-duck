package spharos.nu.etc.domain.review.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReviewTest {

	@Test
	@DisplayName("리뷰 조회 테스트")
	void getReview() {

		// given
		Review review = Review.builder()
			.writerUuid("writer_uuid")
			.receiverUuid("receiver_uuid")
			.goodsCode("20240610")
			.score(50)
			.content("훌륭합니다.")
			.build();

		// when, then
		Assertions.assertThat(review.getWriterUuid()).isEqualTo("writer_uuid");
		Assertions.assertThat(review.getReceiverUuid()).isEqualTo("receiver_uuid");
		Assertions.assertThat(review.getGoodsCode()).isEqualTo("20240610");
		Assertions.assertThat(review.getScore()).isEqualTo(50);
		Assertions.assertThat(review.getContent()).isEqualTo("훌륭합니다.");
	}
}