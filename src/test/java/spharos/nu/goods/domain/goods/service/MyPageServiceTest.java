package spharos.nu.goods.domain.goods.service;

import static org.mockito.BDDMockito.*;

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

import spharos.nu.goods.domain.goods.dto.GoodsInfoDto;
import spharos.nu.goods.domain.goods.dto.GoodsSellResponseDto;
import spharos.nu.goods.domain.goods.repository.GoodsRepository;

@ExtendWith(MockitoExtension.class)
class MyPageServiceTest {

	@Mock
	private GoodsRepository goodsRepository;

	@InjectMocks
	private MyPageService myPageService;

	private String uuid;
	private Integer index;
	private byte statusNum;
	private Pageable pageable;
	private Page<GoodsInfoDto> goodsInfoPage;
	private Page<GoodsWishInfoDto> goodsWishInfoPage;

	@BeforeEach
	void setUp() {
		uuid = "test-uuid";
		index = 0;
		statusNum = 1;
		pageable = PageRequest.of(index, 10);

		List<GoodsInfoDto> goodsList = Arrays.asList(
			new GoodsInfoDto("code1", "url1", "name1", 1000L, true, (byte)1),
			new GoodsInfoDto("code2", "url2", "name2", 2000L, false, (byte)1)
		);

		goodsInfoPage = new PageImpl<>(goodsList, pageable, goodsList.size());

		List<GoodsWishInfoDto> goodsWishList = Arrays.asList(
			new GoodsWishInfoDto("code1", "url1", "name1"),
			new GoodsWishInfoDto("code2", "url2", "name2")
		);

		goodsWishInfoPage = new PageImpl<>(goodsWishList, pageable, goodsWishList.size());
	}

	@Test
	@DisplayName("등록한 상품")
	void testSellGoodsGet() {

		// given
		when(goodsRepository.findAllGoods(eq(uuid), eq(statusNum), any(Pageable.class)))
			.thenReturn(goodsInfoPage);

		// when
		GoodsSellResponseDto response = myPageService.sellGoodsGet(uuid, index, statusNum);

		// then
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getNowPage()).isEqualTo(goodsInfoPage.getNumber());
		Assertions.assertThat(response.getMaxPage()).isEqualTo(goodsInfoPage.getTotalPages());
		Assertions.assertThat(response.getGoodsList()).isEqualTo(goodsInfoPage.getContent());
	}

	@Test
	@DisplayName("관심 상품 조회")
	void testWishGoodsGet() {

		// given
		when(goodsRepository.findWishedGoodsByUuid(eq(uuid), any(Pageable.class)))
			.thenReturn(goodsWishInfoPage);

		// when
		GoodsWishResponseDto res = myPageService.wishGoodsGet(uuid, index);

		// then
		Assertions.assertThat(res).isNotNull();
		Assertions.assertThat(res.getNowPage()).isEqualTo(goodsWishInfoPage.getNumber());
		Assertions.assertThat(res.getMaxPage()).isEqualTo(goodsWishInfoPage.getTotalPages());
		Assertions.assertThat(res.getGoodsList()).isEqualTo(goodsWishInfoPage.getContent());
	}
}