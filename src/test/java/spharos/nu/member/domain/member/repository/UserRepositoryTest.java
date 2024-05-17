package spharos.nu.member.domain.member.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import spharos.nu.member.domain.member.entity.Member;

@DataJpaTest
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("uuid 회원 조회 테스트")
	void findByUuid() {

		// given
		Member savedmember = Member.builder()
			.id(1L)
			.uuid("테스트_uuid")
			.userId("testId")
			.password("test")
			.phoneNumber("010")
			.email("@test.com")
			.nickname("쓰껄쓰껄")
			.profileImage("img_url")
			.favoriteCategory("애니")
			.isNotify(true)
			.isWithdraw(false)
			.build();
		userRepository.save(savedmember);

		// when
		Member member = userRepository.findByUuid("테스트_uuid").orElseThrow();

		// then
		Assertions.assertThat(member.getProfileImage()).isEqualTo("img_url");
		Assertions.assertThat(member.getNickname()).isEqualTo("쓰껄쓰껄");
		Assertions.assertThat(member.getFavoriteCategory()).isEqualTo("애니");
	}
}