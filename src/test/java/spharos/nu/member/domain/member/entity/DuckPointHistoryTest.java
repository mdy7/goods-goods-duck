package spharos.nu.member.domain.member.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DuckPointHistoryTest {

	@Test
	@DisplayName("덕포인트 상세내역")
	void getDuckPointHistoryTest() {

		// given
		DuckPointHistory duckPointHistory = DuckPointHistory.builder()
			.uuid("test_uuid1")
			.leftPoint(50000L)
			.changeAmount(5000L)
			.changeStatus(false)
			.historyDetail("보증금")
			.build();

		// when, then
		Assertions.assertThat(duckPointHistory.getUuid()).isEqualTo("test_uuid1");
		Assertions.assertThat(duckPointHistory.getLeftPoint()).isEqualTo(50000L);
		Assertions.assertThat(duckPointHistory.getChangeAmount()).isEqualTo(5000L);
		Assertions.assertThat(duckPointHistory.getChangeStatus()).isEqualTo(false);
		Assertions.assertThat(duckPointHistory.getHistoryDetail()).isEqualTo("보증금");
	}
}