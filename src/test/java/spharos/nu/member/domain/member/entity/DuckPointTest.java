package spharos.nu.member.domain.member.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DuckPointTest {

	@Test
	@DisplayName("덕포인트 조회")
	void getDuckPoint() {

		// given
		DuckPoint duckPoint = DuckPoint.builder()
			.uuid("test_uuid1")
			.nowPoint(1000000L)
			.build();

		//when, then
		Assertions.assertThat(duckPoint.getUuid()).isEqualTo("test_uuid1");
		Assertions.assertThat(duckPoint.getNowPoint()).isEqualTo(1000000L);
	}

}