package spharos.nu.goods.domain.bid.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BidTest {

	@Test
	@DisplayName("입찰 등록")
	void biddingTest() {

		// given
		Bid bid = Bid.builder()
			.bidderUuid("test")
			.goodsCode("20240604")
			.price(10000L)
			.build();

		// when, then
		Assertions.assertThat(bid.getBidderUuid()).isEqualTo("test");
		Assertions.assertThat(bid.getGoodsCode()).isEqualTo("20240604");
	}

}