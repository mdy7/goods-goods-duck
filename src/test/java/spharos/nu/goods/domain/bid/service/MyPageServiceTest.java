package spharos.nu.goods.domain.bid.service;

import static org.mockito.ArgumentMatchers.*;
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

import spharos.nu.goods.domain.bid.dto.response.BidGoodsCodeDto;
import spharos.nu.goods.domain.bid.repository.BidRepository;
import spharos.nu.goods.domain.mypage.dto.response.BidGoodsResponseDto;
import spharos.nu.goods.domain.mypage.service.MyPageService;

@ExtendWith(MockitoExtension.class)
public class MyPageServiceTest {

	@Mock
	private BidRepository bidRepository;

	@InjectMocks
	private MyPageService myPageService;

	private String uuid;
	private Byte status;
	private Pageable pageable;
	private Page<BidGoodsCodeDto> bidGoodsPage;

	@BeforeEach
	void setUp() {
		uuid = "test-uuid";
		status = 1;
		pageable = PageRequest.of(0, 10);

		List<BidGoodsCodeDto> goodsList = Arrays.asList(
			new BidGoodsCodeDto("code1"),
			new BidGoodsCodeDto("code1")
		);

		bidGoodsPage = new PageImpl<>(goodsList, pageable, goodsList.size());
	}

	@Test
	@DisplayName("입찰한 상품 중복 조회x")
	void testBidGoodsGet() {

		// given
		when(bidRepository.findAllGoods(eq(uuid), any(Pageable.class), eq(status)))
			.thenReturn(bidGoodsPage);

		// when
		BidGoodsResponseDto response = myPageService.bidGoodsGet(uuid, pageable, status);

		// then
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getTotalCount()).isEqualTo(bidGoodsPage.getTotalElements());
		Assertions.assertThat(response.getNowPage()).isEqualTo(bidGoodsPage.getNumber());
		Assertions.assertThat(response.getMaxPage()).isEqualTo(bidGoodsPage.getTotalPages());
		Assertions.assertThat(response.getGoodsList()).isEqualTo(bidGoodsPage.getContent());
	}
}
