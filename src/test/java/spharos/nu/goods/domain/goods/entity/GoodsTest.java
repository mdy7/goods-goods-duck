package spharos.nu.goods.domain.goods.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GoodsTest {

	@Test
	@DisplayName("굿즈 등록 여부")
	void createGoods() {

		// given
		Goods goods = Goods.builder()
			.sellerUuid("test")
			.goodsCode("20240521")
			.name("포카")
			.minPrice(15000L)
			.description("포카 상태 좋아요")
			.openedAt(LocalDateTime.now())
			.closedAt(LocalDateTime.now())
			.wishTradeType((byte)0)
			.tradingStatus((byte)1)
			.isDelete(false)
			.build();

		// when, then
		Assertions.assertThat(goods.getSellerUuid()).isEqualTo("test");
		Assertions.assertThat(goods.getGoodsCode()).isEqualTo("20240521");
		Assertions.assertThat(goods.getName()).isEqualTo("포카");
		Assertions.assertThat(goods.getMinPrice()).isEqualTo(15000L);
		Assertions.assertThat(goods.getDescription()).isEqualTo("포카 상태 좋아요");
		Assertions.assertThat(goods.getWishTradeType()).isEqualTo((byte)0);
		Assertions.assertThat(goods.getTradingStatus()).isEqualTo((byte)1);
	}
}