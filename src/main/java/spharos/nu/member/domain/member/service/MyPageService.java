package spharos.nu.member.domain.member.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import spharos.nu.member.global.exception.CustomException;
import spharos.nu.member.global.exception.errorcode.ErrorCode;

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
			throw new CustomException(ErrorCode.ALREADY_EXIST_USER);
		}
	}

	public ProfileResponseDto profileGet(String uuid) {

		MemberInfo member = getMemberInfo(uuid);

		return ProfileResponseDto.builder()
			.profileImg(member.getProfileImage())
			.nickname(member.getNickname())
			.favCategory(member.getFavoriteCategory())
			.build();
	}

	public Void profileUpdate(String uuid, ProfileRequestDto profileRequestDto) {

		MemberInfo member = getMemberInfo(uuid);

		memberInfoRepository.save(MemberInfo.builder()
			.id(member.getId())
			.profileImage(profileRequestDto.getProfileImage())
			.nickname(profileRequestDto.getNickname())
			.favoriteCategory(profileRequestDto.getFavoriteCategory())
			.isNotify(member.isNotify())
			.build());

		return null;
	}

	public String profileImageGet(String uuid) {

		MemberInfo member = getMemberInfo(uuid);

		return member.getProfileImage();
	}

	public String profileImageUpdate(String uuid, ProfileImageRequestDto profileImageRequestDto) {

		MemberInfo member = getMemberInfo(uuid);
		String imgUrl = profileImageRequestDto.getImgUrl();

		memberInfoRepository.save(MemberInfo.builder()
			.id(member.getId())
			.uuid(member.getUuid())
			.nickname(member.getNickname())
			.profileImage(imgUrl)
			.favoriteCategory(member.getFavoriteCategory())
			.isNotify(member.isNotify())
			.build());

		return imgUrl;
	}

	public Void profileImageDelete(String uuid) {

		MemberInfo member = getMemberInfo(uuid);

		memberInfoRepository.save(MemberInfo.builder()
			.id(member.getId())
			.uuid(member.getUuid())
			.nickname(member.getNickname())
			.profileImage(null)
			.favoriteCategory(member.getFavoriteCategory())
			.isNotify(member.isNotify())
			.build());

		return null;
	}

	public MannerDuckDto mannerDuckGet(String uuid) {

		MemberScore memberScore = scoreRepository.findByUuid(uuid).orElseThrow();
		Integer score = memberScore.getScore();
		Integer level;
		Integer leftPoint;

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
		Page<DuckPointInfoDto> duckPointInfoPage = pointHistoryRepository.findByUuid(uuid, pageable);

		return DuckPointDetailDto.builder()
			.nowPage(duckPointInfoPage.getNumber())
			.maxPage(duckPointInfoPage.getTotalPages())
			.isLast(duckPointInfoPage.isLast())
			.historyList(duckPointInfoPage.getContent())
			.build();
	}

	private MemberInfo getMemberInfo(String uuid) {

		return memberInfoRepository.findByUuid(uuid).orElseThrow();
	}
}
