package spharos.nu.member.domain.member.service;

import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import spharos.nu.member.domain.member.dto.request.ProfileImageRequestDto;
import spharos.nu.member.domain.member.dto.request.ProfileRequestDto;
import spharos.nu.member.domain.member.dto.response.DuckPointDetailDto;
import spharos.nu.member.domain.member.dto.response.DuckPointInfoDto;
import spharos.nu.member.domain.member.dto.response.MannerDuckDto;
import spharos.nu.member.domain.member.dto.response.ProfileResponseDto;
import spharos.nu.member.domain.member.entity.DuckPoint;
import spharos.nu.member.domain.member.entity.MemberInfo;
import spharos.nu.member.domain.member.entity.MemberScore;
import spharos.nu.member.domain.member.repository.DuckPointRepository;
import spharos.nu.member.domain.member.repository.MemberInfoRepository;
import spharos.nu.member.domain.member.repository.PointHistoryRepository;
import spharos.nu.member.domain.member.repository.ScoreRepository;

@ExtendWith(MockitoExtension.class)
class MyPageServiceTest {

	@Mock
	private MemberInfoRepository memberInfoRepository;
	@Mock
	private ScoreRepository scoreRepository;
	@Mock
	private DuckPointRepository duckPointRepository;
	@Mock
	private PointHistoryRepository pointHistoryRepository;

	@InjectMocks
	private MyPageService myPageService;

	private String uuid;
	private Integer index;

	private Pageable pageable;

	private Page<DuckPointInfoDto> duckPointInfoPage;

	@Test
	@DisplayName("회원 프로필 조회")
	void profileGet() {

		// given
		MemberInfo member = MemberInfo.builder()
			.id(1L)
			.uuid("테스트_uuid")
			.nickname("쓰껄쓰껄")
			.profileImage("img_url")
			.favoriteCategory("애니")
			.isNotify(true)
			.build();

		given(memberInfoRepository.findByUuid("테스트_uuid")).willReturn(java.util.Optional.ofNullable(member));

		// when
		ProfileResponseDto profileResponseDto = myPageService.profileGet("테스트_uuid");

		// then
		Assertions.assertThat(profileResponseDto.getProfileImg()).isEqualTo("img_url");
		Assertions.assertThat(profileResponseDto.getNickname()).isEqualTo("쓰껄쓰껄");
		Assertions.assertThat(profileResponseDto.getFavCategory()).isEqualTo("애니");
	}

	@Test
	@DisplayName("프로필 수정 서비스 테스트")
	void testProfileUpdate() {

		// given
		String testUuid = "테스트_uuid";
		MemberInfo member = MemberInfo.builder()
			.uuid(testUuid)
			.nickname("쓰껄쓰껄")
			.profileImage("img_url")
			.favoriteCategory("애니")
			.isNotify(true)
			.build();

		given(memberInfoRepository.findByUuid(testUuid)).willReturn(java.util.Optional.of(member));

		// ArgumentCaptor 인스턴스 생성
		ArgumentCaptor<MemberInfo> argumentCaptor = ArgumentCaptor.forClass(MemberInfo.class);

		// when
		String newImgUrl = "새로운이미지URL";
		String newNickname = "새로운닉네임";
		String newCat = "애니";
		ProfileRequestDto profileRequestDto = new ProfileRequestDto(newImgUrl, newNickname, newCat);
		myPageService.profileUpdate(testUuid, profileRequestDto);

		// then
		// 메서드 호출 검증 및 인자 캡처
		verify(memberInfoRepository).save(argumentCaptor.capture());

		// 캡처된 인자 검증
		MemberInfo capturedMemberInfo = argumentCaptor.getValue();
		Assertions.assertThat(capturedMemberInfo.getProfileImage()).isEqualTo(newImgUrl);
		Assertions.assertThat(capturedMemberInfo.getNickname()).isEqualTo(newNickname);
		Assertions.assertThat(capturedMemberInfo.getFavoriteCategory()).isEqualTo(newCat);
		Assertions.assertThat(capturedMemberInfo.getUuid()).isEqualTo(testUuid);
	}

	@Test
	@DisplayName("프로필 이미지 조회 서비스 테스트")
	void testProfileImageGet() {

		// given
		MemberInfo member = MemberInfo.builder()
			.id(1L)
			.uuid("테스트_uuid")
			.nickname("쓰껄쓰껄")
			.profileImage("img_url")
			.favoriteCategory("애니")
			.isNotify(true)
			.build();

		given(memberInfoRepository.findByUuid("테스트_uuid")).willReturn(java.util.Optional.ofNullable(member));

		// when
		String imageGet = myPageService.profileImageGet("테스트_uuid");

		// then
		Assertions.assertThat(imageGet).isEqualTo("img_url");
	}

	@Test
	@DisplayName("프로필이미지 수정 서비스 테스트")
	void testProfileImageUpdate() {

		// given
		String testUuid = "테스트_uuid";
		MemberInfo member = MemberInfo.builder()
			.uuid(testUuid)
			.nickname("쓰껄쓰껄")
			.profileImage("img_url")
			.favoriteCategory("애니")
			.isNotify(true)
			.build();

		given(memberInfoRepository.findByUuid(testUuid)).willReturn(java.util.Optional.of(member));

		// when
		String newImgUrl = "새로운이미지URL";
		ProfileImageRequestDto profileImageRequestDto = new ProfileImageRequestDto(newImgUrl);
		String imageUpdate = myPageService.profileImageUpdate(testUuid, profileImageRequestDto);

		// then
		Assertions.assertThat(imageUpdate).isEqualTo(newImgUrl);
	}

	@Test
	@DisplayName("프로필이미지 삭제 서비스 테스트")
	void testProfileImageDelete() {

		// given
		MemberInfo member = MemberInfo.builder()
			.id(1L)
			.uuid("테스트_uuid")
			.nickname("쓰껄쓰껄")
			.profileImage("img_url")
			.favoriteCategory("애니")
			.isNotify(true)
			.build();

		given(memberInfoRepository.findByUuid("테스트_uuid")).willReturn(java.util.Optional.ofNullable(member));
		given(memberInfoRepository.save(any(MemberInfo.class))).willAnswer(invocation -> invocation.getArgument(0));

		// when
		myPageService.profileImageDelete("테스트_uuid");

		// then
		ArgumentCaptor<MemberInfo> captor = ArgumentCaptor.forClass(MemberInfo.class);
		verify(memberInfoRepository).save(captor.capture());
		MemberInfo savedMember = captor.getValue();

		Assertions.assertThat(savedMember.getProfileImage()).isEqualTo(null);
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

	@Test
	@DisplayName("덕포인트 상세내역 조회")
	void testDuckPointDetailGet() {

		// given
		List<DuckPointInfoDto> pointsList = Arrays.asList(
			DuckPointInfoDto.builder()
				.changeAmount(5000L)
				.leftPoint(50000L)
				.changeStatus(false)
				.historyDetail("보증금")
				.createdAt(LocalDateTime.now())
				.build(),
			DuckPointInfoDto.builder()
				.changeAmount(3000L)
				.leftPoint(53000L)
				.changeStatus(true)
				.historyDetail("입금")
				.createdAt(LocalDateTime.now())
				.build()
		);

		uuid = "test_uuid";
		index = 0;
		pageable = PageRequest.of(index, 10, Sort.by("createdAt").descending());
		duckPointInfoPage = new PageImpl<>(pointsList, pageable, pointsList.size());

		when(pointHistoryRepository.findByUuid(eq(uuid), any(Pageable.class))).thenReturn(duckPointInfoPage);

		// when
		DuckPointDetailDto res = myPageService.duckPointDetailGet(uuid, index);

		// then
		Assertions.assertThat(res).isNotNull();
		Assertions.assertThat(res.getNowPage()).isEqualTo(duckPointInfoPage.getNumber());
		Assertions.assertThat(res.getMaxPage()).isEqualTo(duckPointInfoPage.getTotalPages());
		Assertions.assertThat(res.getHistoryList()).isEqualTo(duckPointInfoPage.getContent());

	}
}