package spharos.nu.member.domain.member.service;

import static org.mockito.BDDMockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import spharos.nu.member.domain.member.dto.MannerDuckDto;
import spharos.nu.member.domain.member.dto.ProfileResponseDto;
import spharos.nu.member.domain.member.entity.DuckPoint;
import spharos.nu.member.domain.member.entity.Member;
import spharos.nu.member.domain.member.entity.MemberScore;
import spharos.nu.member.domain.member.repository.DuckPointRepository;
import spharos.nu.member.domain.member.repository.ScoreRepository;
import spharos.nu.member.domain.member.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class MyPageServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private ScoreRepository scoreRepository;
	@Mock
	private DuckPointRepository duckPointRepository;

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

	@Test
	@DisplayName("매너덕 조회 서비스 테스트")
	void mannerDuckGet() {

		// given
		MemberScore memberScore1 = MemberScore.builder()
			.uuid("testing1")
			.score(50)
			.build();
		given(scoreRepository.findByUuid("testing1")).willReturn(java.util.Optional.ofNullable(memberScore1));

		MemberScore memberScore2 = MemberScore.builder()
			.uuid("testing2")
			.score(90)
			.build();
		given(scoreRepository.findByUuid("testing2")).willReturn(java.util.Optional.ofNullable(memberScore2));

		// when
		MannerDuckDto mannerDuckDto1 = myPageService.mannerDuckGet("testing1");
		MannerDuckDto mannerDuckDto2 = myPageService.mannerDuckGet("testing2");

		//then
		Assertions.assertThat(mannerDuckDto1.getLevel()).isEqualTo(3);
		Assertions.assertThat(mannerDuckDto1.getLeftPoint()).isEqualTo(10);

		Assertions.assertThat(mannerDuckDto2.getLevel()).isEqualTo(5);
		Assertions.assertThat(mannerDuckDto2.getLeftPoint()).isEqualTo(0);
	}

	@Test
	@DisplayName("덕포인트 조회")
	void duckPointGet() {

		// given
		DuckPoint duckPoint = DuckPoint.builder()
			.uuid("test_uuid1")
			.nowPoint(50000L)
			.build();
		given(duckPointRepository.findByUuid("test_uuid1")).willReturn(java.util.Optional.ofNullable(duckPoint));

		// when
		Long nowPoint = myPageService.duckPointGet("test_uuid1");

		// when
		Assertions.assertThat(nowPoint).isEqualTo(50000L);
	}
}