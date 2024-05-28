package spharos.nu.member.domain.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.member.domain.member.dto.DuckPointDetailDto;
import spharos.nu.member.domain.member.dto.DuckPointInfoDto;
import spharos.nu.member.domain.member.dto.MannerDuckDto;
import spharos.nu.member.domain.member.dto.ProfileResponseDto;
import spharos.nu.member.domain.member.entity.DuckPoint;
import spharos.nu.member.domain.member.entity.Member;
import spharos.nu.member.domain.member.entity.MemberScore;
import spharos.nu.member.domain.member.repository.DuckPointRepository;
import spharos.nu.member.domain.member.repository.PointHistoryRepository;
import spharos.nu.member.domain.member.repository.ScoreRepository;
import spharos.nu.member.domain.member.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

	private final UserRepository userRepository;
	private final ScoreRepository scoreRepository;
	private final PointHistoryRepository pointHistoryRepository;
	private final DuckPointRepository duckPointRepository;

	public ProfileResponseDto profileGet(String uuid) {

		Member member = userRepository.findByUuid(uuid).orElseThrow();

		return ProfileResponseDto.builder()
			.profileImg(member.getProfileImage())
			.nickname(member.getNickname())
			.favCategory(member.getFavoriteCategory())
			.build();
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
}
