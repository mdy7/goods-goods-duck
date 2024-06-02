package spharos.nu.member.domain.member.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberInfoTest {

	@Test
	@DisplayName("회원 생성 여부")
	void createMember() {

		/*
		given
		 */
		MemberInfo member = MemberInfo.builder()
			.uuid("createMember")
			.nickname("테스트어렵다")
			.profileImage("imgurl")
			.favoriteCategory("아이돌")
			.isNotify(true)
			.build();

		/*
		when, then
		 */
		Assertions.assertThat(member.getUuid()).isEqualTo("createMember");
		Assertions.assertThat(member.getNickname()).isEqualTo("테스트어렵다");
		Assertions.assertThat(member.getProfileImage()).isEqualTo("imgurl");
		Assertions.assertThat(member.getFavoriteCategory()).isEqualTo("아이돌");
		Assertions.assertThat(member.isNotify()).isEqualTo(true);
	}
}