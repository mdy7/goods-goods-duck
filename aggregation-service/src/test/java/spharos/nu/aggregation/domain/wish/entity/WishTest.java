package spharos.nu.aggregation.domain.wish.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WishTest {

	@Test
	@DisplayName("좋아요 등록")
	void wishTest() {

		// given
		Wish wish = Wish.builder()
			.uuid("test")
			.goodsCode("20240603")
			.build();

		// when, then
		Assertions.assertThat(wish.getUuid()).isEqualTo("test");
		Assertions.assertThat(wish.getGoodsCode()).isEqualTo("20240603");
	}

}