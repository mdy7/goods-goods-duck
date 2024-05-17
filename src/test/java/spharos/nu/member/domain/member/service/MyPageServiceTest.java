package spharos.nu.member.domain.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import spharos.nu.member.domain.member.dto.ProfileResponseDto;
import spharos.nu.member.domain.member.entity.Member;
import spharos.nu.member.domain.member.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class MyPageServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private MyPageService myPageService;

	@Test
	@DisplayName("회원 프로필 조회")
	void profileGet() {

		// given
		Member member = Member.builder()
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

		given(userRepository.findByUuid("테스트_uuid")).willReturn(java.util.Optional.ofNullable(member));

		// when
		ProfileResponseDto profileResponseDto = myPageService.profileGet("테스트_uuid");

		// then
		Assertions.assertThat(profileResponseDto.getProfileImg()).isEqualTo("img_url");
		Assertions.assertThat(profileResponseDto.getNickname()).isEqualTo("쓰껄쓰껄");
		Assertions.assertThat(profileResponseDto.getFavCategory()).isEqualTo("애니");
	}
}