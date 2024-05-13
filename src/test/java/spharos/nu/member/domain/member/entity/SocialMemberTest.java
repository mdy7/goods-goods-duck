package spharos.nu.member.domain.member.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SocialMemberTest {

	@Test
	@DisplayName("간편 회원 생성 여부")
	void createSocialMember() {

		/*
		given
		 */
		SocialMember socialMember = SocialMember.builder()
			.uuid("socialMember")
			.memberCode("이게맞나")
			.socialMemberType((byte)1)
			.build();

		/*
		when, then
		 */
		Assertions.assertThat(socialMember.getUuid()).isEqualTo("socialMember");
		Assertions.assertThat(socialMember.getMemberCode()).isEqualTo("이게맞나");
		Assertions.assertThat(socialMember.getSocialMemberType()).isEqualTo((byte)1);
	}
}