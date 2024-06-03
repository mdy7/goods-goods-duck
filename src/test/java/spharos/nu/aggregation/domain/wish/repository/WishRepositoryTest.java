package spharos.nu.aggregation.domain.wish.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.querydsl.jpa.impl.JPAQueryFactory;

import spharos.nu.aggregation.domain.config.TestQueryDslConfig;
import spharos.nu.aggregation.domain.wish.dto.GoodsCodeDto;
import spharos.nu.aggregation.domain.wish.entity.Wish;

@DataJpaTest
@Import(TestQueryDslConfig.class)
class WishRepositoryTest {

	@Autowired
	private WishRepository wishRepository;

	@Autowired
	private JPAQueryFactory queryFactory;

	private String uuid;
	private Integer index;
	private Pageable pageable;

	@BeforeEach
	public void setUp() {

		wishRepository.save(Wish.builder()
			.uuid("test-uuid1")
			.goodsCode("20240603")
			.build());

		wishRepository.save(Wish.builder()
			.uuid("test-uuid2")
			.goodsCode("20240604")
			.build());
	}

	@AfterEach
	public void tearDown() {

		// 테스트 데이터 정리
		wishRepository.deleteAll();
	}

	@Test
	@DisplayName("관심 상품 조회")
	void testFindWishedGoodsByUuid() {

		// given
		uuid = "test-uuid1";
		index = 0;
		pageable = PageRequest.of(0, 10);

		// then
		Page<GoodsCodeDto> result = wishRepository.findWishedGoodsByUuid(uuid, pageable);


		// when
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getContent()).isNotEmpty();
		Assertions.assertThat(result.getContent().get(0).getGoodsCode()).isEqualTo("20240603");
	}
}