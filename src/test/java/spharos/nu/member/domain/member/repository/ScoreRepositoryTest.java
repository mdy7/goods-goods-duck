package spharos.nu.member.domain.member.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import spharos.nu.member.domain.member.entity.MemberScore;

@DataJpaTest
class ScoreRepositoryTest {

	@Autowired
	private ScoreRepository scoreRepository;

	@Test
	@DisplayName("uuid 회원 점수 조회 테스트")
	void findByUuid() {

		// given
		MemberScore savedScore = MemberScore.builder()
			.uuid("testing")
			.score(50)
			.build();
		scoreRepository.save(savedScore);

		// when
		MemberScore memberScore = scoreRepository.findByUuid("testing").orElseThrow();

		// then
		Assertions.assertThat(memberScore.getScore()).isEqualTo(50);
	}
}