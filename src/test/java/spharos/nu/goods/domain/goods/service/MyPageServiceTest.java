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

import spharos.nu.goods.domain.goods.dto.GoodsCodeDto;
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
	private Page<GoodsCodeDto> goodsCodePage;

	@BeforeEach
	void setUp() {
		uuid = "test-uuid";
		index = 0;
		statusNum = 1;
		pageable = PageRequest.of(index, 10);

		List<GoodsCodeDto> goodsList = Arrays.asList(
			new GoodsCodeDto("code1"),
			new GoodsCodeDto("code2")
		);

		goodsCodePage = new PageImpl<>(goodsList, pageable, goodsList.size());
	}

	@Test
	@DisplayName("등록한 상품")
	void testSellGoodsGet() {

		// given
		when(goodsRepository.findAllGoods(eq(uuid), eq(statusNum), any(Pageable.class)))
			.thenReturn(goodsCodePage);

		// when
		GoodsSellResponseDto response = myPageService.sellGoodsGet(uuid, index, statusNum);

		// then
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getTotalCount()).isEqualTo(goodsCodePage.getTotalElements());
		Assertions.assertThat(response.getNowPage()).isEqualTo(goodsCodePage.getNumber());
		Assertions.assertThat(response.getMaxPage()).isEqualTo(goodsCodePage.getTotalPages());
		Assertions.assertThat(response.getGoodsList()).isEqualTo(goodsCodePage.getContent());
	}

}