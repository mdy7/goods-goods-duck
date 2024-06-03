package spharos.nu.aggregation.domain.wish.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import spharos.nu.aggregation.domain.wish.dto.GoodsCodeDto;
import spharos.nu.aggregation.domain.wish.dto.WishGoodsResponseDto;
import spharos.nu.aggregation.domain.wish.repository.WishRepository;

@ExtendWith(MockitoExtension.class)
class MyPageServiceTest {

	@Mock
	WishRepository wishRepository;

	@InjectMocks
	MyPageService myPageService;

	private String uuid;
	private Integer index;
	private Pageable pageable;
	private Page<GoodsCodeDto> goodsCodePage;

	@BeforeEach
	void setUp() {
		uuid = "test-uuid";
		index = 0;
		pageable = PageRequest.of(index, 10);

		List<GoodsCodeDto> goodsWishList = Arrays.asList(
			new GoodsCodeDto("code1"),
			new GoodsCodeDto("code2")
		);

		goodsCodePage = new PageImpl<>(goodsWishList, pageable, goodsWishList.size());
	}

	@Test
	@DisplayName("관심 상품 조회")
	void testWishGoodsGet() {

		// given
		when(wishRepository.findWishedGoodsByUuid(eq(uuid), any(Pageable.class)))
			.thenReturn(goodsCodePage);

		// when
		WishGoodsResponseDto res = myPageService.wishGoodsGet(uuid, index);

		// then
		Assertions.assertThat(res).isNotNull();
		Assertions.assertThat(res.getNowPage()).isEqualTo(goodsCodePage.getNumber());
		Assertions.assertThat(res.getMaxPage()).isEqualTo(goodsCodePage.getTotalPages());
		Assertions.assertThat(res.getGoodsList()).isEqualTo(goodsCodePage.getContent());
	}
}