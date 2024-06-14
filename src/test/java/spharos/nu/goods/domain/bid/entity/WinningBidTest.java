package spharos.nu.goods.domain.bid.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WinningBidTest {

	@Test
	@DisplayName("낙찰 등록")
	void winningBidTest() {

		// given
		WinningBid winningBid = WinningBid.builder()
			.bidderUuid("test_uuid")
			.sellerUuid("seller_uuid")
			.goodsCode("testGoods")
			.winningPrice(25000L)
			.build();

		// when, then
		Assertions.assertThat(winningBid.getBidderUuid()).isEqualTo("test_uuid");
		Assertions.assertThat(winningBid.getSellerUuid()).isEqualTo("seller_uuid");
		Assertions.assertThat(winningBid.getGoodsCode()).isEqualTo("testGoods");
		Assertions.assertThat(winningBid.getWinningPrice()).isEqualTo(25000L);
	}

}