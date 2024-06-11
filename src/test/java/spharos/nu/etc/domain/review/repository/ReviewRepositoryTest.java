package spharos.nu.etc.domain.review.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import spharos.nu.etc.domain.review.dto.response.ReviewListDto;
import spharos.nu.etc.domain.review.entity.Review;
import spharos.nu.etc.global.exception.CustomException;
import spharos.nu.etc.global.exception.errorcode.ErrorCode;

@DataJpaTest
class ReviewRepositoryTest {

	@Autowired
	private ReviewRepository reviewRepository;

	private String receiverUuid;
	private Integer index;
	private Pageable pageable;

	@BeforeEach
	void setUp() {

		Review review1 = Review.builder()
			.writerUuid("writer_uuid1")
			.receiverUuid("receiver_uuid1")
			.goodsCode("20240610")
			.score(50)
			.content("훌륭합니다.")
			.build();
		reviewRepository.save(review1);

		Review review2 = Review.builder()
			.writerUuid("writer_uuid2")
			.receiverUuid("receiver_uuid1")
			.goodsCode("20240611")
			.score(50)
			.content("훌륭합니다.")
			.build();
		reviewRepository.save(review2);

		System.out.printf("@@@@@@@@@@@@@@@@@@@@@@@" + review1);
	}

	@AfterEach
	public void tearDown() {

		// 테스트 데이터 정리
		reviewRepository.deleteAll();
	}

	@Test
	@DisplayName("receiver uuid repository 테스트")
	void findByReceiverUuidOrderByCreatedAtDesc() {

		// given
		receiverUuid = "receiver_uuid1";
		index = 0;
		pageable = PageRequest.of(index, 10);

		// then
		Page<ReviewListDto> res = reviewRepository.findByReceiverUuidOrderByCreatedAtDesc(receiverUuid, pageable);

		// when
		Assertions.assertThat(res).isNotNull();
		Assertions.assertThat(res.getContent()).isNotEmpty();
		Assertions.assertThat(res.getContent().size()).isEqualTo(2);
		Assertions.assertThat(res.getContent().get(0).getGoodsCode()).isEqualTo("20240611");
		Assertions.assertThat(res.getContent().get(1).getGoodsCode()).isEqualTo("20240610");
	}

	@Test
	@DisplayName("writeruuid, goodscode repository 테스트")
	void findByWriterUuidAndGoodsCode() {

		// given
		String writerUuid = "writer_uuid1";
		String goodsCode = "20240610";

		// when & then
		CustomException thrownException = assertThrows(CustomException.class, () -> {
			reviewRepository.findByWriterUuidAndGoodsCode(writerUuid, goodsCode)
				.ifPresent(review -> {
					throw new CustomException(ErrorCode.ALREADY_REVIEW_CREATE);
				});
		});

		// 예외 메시지 검증
		Assertions.assertThat(thrownException.getMessage()).isEqualTo(ErrorCode.ALREADY_REVIEW_CREATE.getMessage());
	}
}