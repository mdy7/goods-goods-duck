package spharos.nu.goods.domain.goods.repository;

import java.time.LocalDateTime;

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

import spharos.nu.goods.domain.goods.config.TestQueryDslConfig;
import spharos.nu.goods.domain.goods.entity.Goods;
import spharos.nu.goods.domain.goods.entity.Image;

@DataJpaTest
// @ExtendWith(SpringExtension.class)
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // 실제 데이터베이스 사용
@Import(TestQueryDslConfig.class)
class GoodsRepositoryTest {

	@Autowired
	private GoodsRepository goodsRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private WishRepository wishRepository;

	@Autowired
	private JPAQueryFactory queryFactory;

	private String uuid;
	private Integer index;
	private Pageable pageable;

	@BeforeEach
	public void setUp() {

		goodsRepository.save(Goods.builder()
			.uuid("test-uuid1")
			.code("20240522")
			.name("포카")
			.minPrice(15000L)
			.description("포카 상태 좋아요")
			.openedAt(LocalDateTime.now())
			.closedAt(LocalDateTime.now())
			.durationTime((byte) 1)
			.wishTradeType((byte) 0)
			.tradingStatus((byte) 1)
			.winningPrice(null)
			.categoryId(0L)
			.isDelete(false)
			.build());
		imageRepository.save(Image.builder()
			.code("20240522")
			.url("photo1")
			.index(0)
			.build());
		wishRepository.save(Wish.builder()
			.uuid("test-uuid1")
			.code("20240522")
			.build());


		goodsRepository.save(Goods.builder()
			.uuid("test-uuid2")
			.code("20240523")
			.name("포카2")
			.minPrice(20000L)
			.description("포카2 상태 좋아요")
			.openedAt(LocalDateTime.now())
			.closedAt(LocalDateTime.now())
			.durationTime((byte) 1)
			.wishTradeType((byte) 0)
			.tradingStatus((byte) 1)
			.winningPrice(null)
			.categoryId(0L)
			.isDelete(false)
			.build());
		imageRepository.save(Image.builder()
			.code("20240523")
			.url("photo2")
			.index(0)
			.build());
		wishRepository.save(Wish.builder()
			.uuid("test-uuid2")
			.code("20240523")
			.build());
	}

	@AfterEach
	public void tearDown() {

		// 테스트 데이터 정리
		goodsRepository.deleteAll();
	}

	@Test
	@DisplayName("관심 상품 조회")
	void testFindWishedGoodsByUuid() {

		// given
		uuid = "test-uuid1";
		index = 0;
		pageable = PageRequest.of(0, 10);

		// then
		Page<GoodsWishInfoDto> result = goodsRepository.findWishedGoodsByUuid(uuid, pageable);


		// when
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getContent()).isNotEmpty();
		Assertions.assertThat(result.getContent().get(0).getGoodsCode()).isEqualTo("20240522");
	}
}