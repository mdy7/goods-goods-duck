package spharos.nu.member.domain.member.service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.member.domain.member.dto.request.ProfileImageRequestDto;
import spharos.nu.member.domain.member.dto.request.ProfileRequestDto;
import spharos.nu.member.domain.member.dto.response.DuckPointDetailDto;
import spharos.nu.member.domain.member.dto.response.DuckPointInfoDto;
import spharos.nu.member.domain.member.dto.response.MannerDuckDto;
import spharos.nu.member.domain.member.dto.response.ProfileResponseDto;
import spharos.nu.member.domain.member.entity.DuckPoint;
import spharos.nu.member.domain.member.entity.DuckPointHistory;
import spharos.nu.member.domain.member.entity.MemberInfo;
import spharos.nu.member.domain.member.entity.MemberScore;
import spharos.nu.member.domain.member.kafka.dto.JoinRequestDto;
import spharos.nu.member.domain.member.repository.DuckPointRepository;
import spharos.nu.member.domain.member.repository.MemberInfoRepository;
import spharos.nu.member.domain.member.repository.PointHistoryRepository;
import spharos.nu.member.domain.member.repository.ScoreRepository;
import spharos.nu.member.global.exception.CustomException;

import static spharos.nu.member.global.exception.errorcode.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

	private final MemberInfoRepository memberInfoRepository;
	private final ScoreRepository scoreRepository;
	private final PointHistoryRepository pointHistoryRepository;
	private final DuckPointRepository duckPointRepository;

	public void isDuplicatedNick(String nickname) {
		Optional<MemberInfo> isMember = memberInfoRepository.findByNickname(nickname);
		if (isMember.isPresent()) {
			throw new CustomException(ALREADY_EXIST_USER);
		}
	}


	/**
	 * 회원가입 시 유저 정보 저장
	 */
	@Transactional
	public void saveUserInfo(JoinRequestDto joinRequestDto) {
		MemberInfo memberInfo = MemberInfo.builder()
				.uuid(joinRequestDto.getUuid())
				.nickname(joinRequestDto.getNickname())
				.profileImage(joinRequestDto.getProfileImage())
				.favoriteCategory(joinRequestDto.getFavoriteCategory())
				.build();
		memberInfoRepository.save(memberInfo);

		DuckPoint duckPoint = DuckPoint.builder()
				.uuid(joinRequestDto.getUuid())
				.nowPoint(0L)
				.build();
		duckPointRepository.save(duckPoint);

		MemberScore memberScore = MemberScore.builder()
				.uuid(joinRequestDto.getUuid())
				.score(50)
				.build();
		scoreRepository.save(memberScore);

		throw new RuntimeException("에러 발생");
	}

		public ProfileResponseDto profileGet(String uuid) {

			MemberInfo member = getMemberInfo(uuid);

			return ProfileResponseDto.builder()
					.userUuid(member.getUuid())
					.profileImage(member.getProfileImage())
					.nickname(member.getNickname())
					.favCategory(member.getFavoriteCategory())
					.build();
		}

		public ProfileResponseDto profileUpdate(String uuid, ProfileRequestDto profileRequestDto) {

			MemberInfo member = getMemberInfo(uuid);
			String profileImage = profileRequestDto.getProfileImage();
			String nickname = profileRequestDto.getNickname();
			String favCategory = profileRequestDto.getFavoriteCategory();

			memberInfoRepository.save(MemberInfo.builder()
					.id(member.getId())
					.uuid(uuid)
					.profileImage(profileImage)
					.nickname(nickname)
					.favoriteCategory(favCategory)
					.build());

			return ProfileResponseDto.builder()
					.userUuid(uuid)
					.profileImage(profileImage)
					.nickname(nickname)
					.favCategory(favCategory)
					.build();
		}

		public String profileImageGet(String uuid) {

			MemberInfo member = getMemberInfo(uuid);

			return member.getProfileImage();
		}

		public String profileImageUpdate(String uuid, ProfileImageRequestDto profileImageRequestDto) {

			MemberInfo member = getMemberInfo(uuid);
			String imgUrl = profileImageRequestDto.getProfileImage();

			memberInfoRepository.save(MemberInfo.builder()
					.id(member.getId())
					.uuid(member.getUuid())
					.nickname(member.getNickname())
					.profileImage(imgUrl)
					.favoriteCategory(member.getFavoriteCategory())
					.build());

			return imgUrl;
		}

		// public Void profileImageDelete(String uuid) {
		//
		// 	MemberInfo member = getMemberInfo(uuid);
		//
		// 	memberInfoRepository.save(MemberInfo.builder()
		// 		.id(member.getId())
		// 		.uuid(member.getUuid())
		// 		.nickname(member.getNickname())
		// 		.profileImage(null)
		// 		.favoriteCategory(member.getFavoriteCategory())
		// 		.build());
		//
		// 	return null;
		// }

		public MannerDuckDto mannerDuckGet(String uuid) {

			List<MemberScore> memberScores = scoreRepository.findAllByUuid(uuid);

			// 점수들의 평균을 계산
			OptionalDouble average = memberScores.stream()
					.mapToInt(MemberScore::getScore)
					.average();

			// 평균을 반올림하여 정수로 반환, 값이 없을 경우 0 반환
			int score = (int)Math.round(average.orElse(0.0));

			int level;
			int leftPoint;

			if (score >= 80) {
				level = 5;
				leftPoint = 0;
			} else if (score >= 60) {
				level = 4;
				leftPoint = 80 - score;
			} else if (score >= 40) {
				level = 3;
				leftPoint = 60 - score;
			} else if (score >= 20) {
				level = 2;
				leftPoint = 40 - score;
			} else {
				level = 1;
				leftPoint = 20 - score;
			}

			return MannerDuckDto.builder()
					.level(level)
					.leftPoint(leftPoint)
					.build();
		}

		public Long duckPointGet(String uuid) {

			DuckPoint duckPoint = duckPointRepository.findByUuid(uuid).orElseThrow();

			return duckPoint.getNowPoint();
		}

		public DuckPointDetailDto duckPointDetailGet(String uuid, Integer index) {

			Pageable pageable = PageRequest.of(index, 10, Sort.by("createdAt").descending());
			Page<DuckPointHistory> duckPointInfoPage = pointHistoryRepository.findByUuid(uuid, pageable);

			List<DuckPointInfoDto> historyList = duckPointInfoPage.getContent().stream()
					.map(history -> DuckPointInfoDto.builder()
							.changeAmount(history.getChangeAmount())
							.leftPoint(history.getLeftPoint())
							.changeStatus(history.getChangeStatus())
							.historyDetail(history.getHistoryDetail())
							.createdAt(history.getCreatedAt())
							.build())
					.toList();

			return DuckPointDetailDto.builder()
					.nowPage(duckPointInfoPage.getNumber())
					.maxPage(duckPointInfoPage.getTotalPages())
					.isLast(duckPointInfoPage.isLast())
					.historyList(historyList)
					.build();
		}

//		public void memberScoreCreate(MemberScoreEventDto memberScoreEventDto) {
//
//			MemberScore memberScore = MemberScore.builder()
//					.uuid(memberScoreEventDto.getReceiverUuid())
//					.score(memberScoreEventDto.getScore())
//					.build();
//			scoreRepository.save(memberScore);
//		}

		private MemberInfo getMemberInfo(String uuid) {

			return memberInfoRepository.findByUuid(uuid).orElseThrow();
		}
}
