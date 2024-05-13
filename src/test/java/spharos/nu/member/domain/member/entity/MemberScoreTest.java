package spharos.nu.member.domain.member.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberScoreTest {

	@Test
	@DisplayName("회원 점수 확인")
	void getMemberScoreTest() {

		/*
		given
		 */
		MemberScore memberScore = MemberScore.builder()
			.uuid("scoreTest")
			.score(50)
			.complainCount(0)
			.build();

		/*
		when, then
		 */
		Assertions.assertThat(memberScore.getUuid()).isEqualTo("scoreTest");
		Assertions.assertThat(memberScore.getScore()).isEqualTo(50);
		Assertions.assertThat(memberScore.getComplainCount()).isEqualTo(0);
	}
}