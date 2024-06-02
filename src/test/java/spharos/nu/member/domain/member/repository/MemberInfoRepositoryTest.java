package spharos.nu.member.domain.member.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import spharos.nu.member.domain.member.entity.MemberInfo;

@DataJpaTest
class MemberInfoRepositoryTest {

	@Autowired
	private MemberInfoRepository memberInfoRepository;

	@Test
	@DisplayName("uuid 회원 조회 테스트")
	void findByUuid() {

		// given
		MemberInfo savedmember = MemberInfo.builder()
			.id(1L)
			.uuid("테스트_uuid")
			.nickname("쓰껄쓰껄")
			.profileImage("img_url")
			.favoriteCategory("애니")
			.isNotify(true)
			.build();
		memberInfoRepository.save(savedmember);

		// when
		MemberInfo member = memberInfoRepository.findByUuid("테스트_uuid").orElseThrow();

		// then
		Assertions.assertThat(member.getProfileImage()).isEqualTo("img_url");
		Assertions.assertThat(member.getNickname()).isEqualTo("쓰껄쓰껄");
		Assertions.assertThat(member.getFavoriteCategory()).isEqualTo("애니");
	}
}