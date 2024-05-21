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
	}

	@Test
	@DisplayName("등록한 상품")
	void testSellGoodsGet() {
		// Given
		when(goodsRepository.findAllGoods(eq(uuid), eq(statusNum), any(Pageable.class)))
			.thenReturn(goodsInfoPage);

		// When
		GoodsSellResponseDto response = myPageService.SellGoodsGet(uuid, index, statusNum);

		// Then
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getNowPage()).isEqualTo(goodsInfoPage.getNumber());
		Assertions.assertThat(response.getMaxPage()).isEqualTo(goodsInfoPage.getTotalPages());
		Assertions.assertThat(response.getGoodsList()).isEqualTo(goodsInfoPage.getContent());
	}
}