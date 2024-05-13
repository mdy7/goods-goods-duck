package spharos.nu.member.domain.member.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

	@Test
	@DisplayName("회원 생성 여부")
	void createMember() {

		/*
		given
		 */
		Member member = Member.builder()
			.uuid("createMember")
			.userId("testId")
			.password("1234")
			.phoneNumber("01000000000")
			.email("test@test.com")
			.nickname("테스트어렵다")
			.profileImage("imgurl")
			.favoriteCategory("아이돌")
			.isNotify(true)
			.isWithdraw(false)
			.build();

		/*
		when, then
		 */
		Assertions.assertThat(member.getUuid()).isEqualTo("createMember");
		Assertions.assertThat(member.getUserId()).isEqualTo("testId");
		Assertions.assertThat(member.getPassword()).isEqualTo("1234");
		Assertions.assertThat(member.getPhoneNumber()).isEqualTo("01000000000");
		Assertions.assertThat(member.getEmail()).isEqualTo("test@test.com");
		Assertions.assertThat(member.getNickname()).isEqualTo("테스트어렵다");
		Assertions.assertThat(member.getProfileImage()).isEqualTo("imgurl");
		Assertions.assertThat(member.getFavoriteCategory()).isEqualTo("아이돌");
		Assertions.assertThat(member.isNotify()).isEqualTo(true);
		Assertions.assertThat(member.isWithdraw()).isEqualTo(false);
	}
}