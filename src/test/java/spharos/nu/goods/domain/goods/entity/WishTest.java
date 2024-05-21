package spharos.nu.goods.domain.goods.entity;

import static org.junit.jupiter.api.Assertions.*;

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
			.code("20240521")
			.build();

		// when, then
		Assertions.assertThat(wish.getUuid()).isEqualTo("test");
		Assertions.assertThat(wish.getCode()).isEqualTo("20240521");
	}
}