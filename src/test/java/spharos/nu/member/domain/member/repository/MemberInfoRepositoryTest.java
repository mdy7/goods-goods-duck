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

	@Test
	@DisplayName("프로필이미지 수정 테스트")
	void profileImageUpdateTest() {

		// given
		MemberInfo member = MemberInfo.builder()
			.uuid("test-uuid")
			.nickname("테스트닉네임")
			.profileImage("기존이미지URL")
			.favoriteCategory("취미")
			.isNotify(true)
			.build();
		member = memberInfoRepository.save(member);
		Long originalId = member.getId();

		// when
		String newImgUrl = "새로운이미지URL";
		memberInfoRepository.save(MemberInfo.builder()
			.id(member.getId())
			.uuid(member.getUuid())
			.nickname(member.getNickname())
			.profileImage(newImgUrl)
			.favoriteCategory(member.getFavoriteCategory())
			.isNotify(member.isNotify())
			.build());

		MemberInfo updatedMember = memberInfoRepository.findByUuid("test-uuid").get();

		// then
		Assertions.assertThat(updatedMember.getId()).isEqualTo(originalId);
		Assertions.assertThat(updatedMember.getProfileImage()).isEqualTo(newImgUrl);
	}
}