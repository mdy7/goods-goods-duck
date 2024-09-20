package spharos.nu.member.domain.member.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.member.domain.member.dto.response.AllMemberResponseDto;
import spharos.nu.member.domain.member.dto.response.ProfileResponseDto;
import spharos.nu.member.domain.member.entity.BlackMember;
import spharos.nu.member.domain.member.entity.MemberInfo;
import spharos.nu.member.domain.member.repository.BlackMemberRepository;
import spharos.nu.member.domain.member.repository.MemberInfoRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

	private final MemberInfoRepository memberInfoRepository;
	private final BlackMemberRepository blackMemberRepository;

	/**
	 * 전체 회원 조회
	 * 블랙 회원 필터링
	 */
	public AllMemberResponseDto allMemberGet(Pageable pageable, boolean isBlack) {

		Page<MemberInfo> memberPage = memberInfoRepository.findAll(pageable);
		List<ProfileResponseDto> memberList;

		if (isBlack) {
			// 블랙 멤버 UUID 목록 가져오기
			List<String> blackMemberUuids = blackMemberRepository.findAll().stream()
				.map(BlackMember::getUuid)
				.toList();

			// 블랙 멤버 필터링
			memberList = memberPage.getContent().stream()
				.filter(member -> blackMemberUuids.contains(member.getUuid()))
				.map(this::ToProfileResponseDto)
				.toList();
		} else {
			// 전체 멤버 조회
			memberList = memberPage.getContent().stream()
				.map(this::ToProfileResponseDto)
				.toList();
		}

		return AllMemberResponseDto.builder()
			.totalCount(memberPage.getTotalElements())
			.nowPage(memberPage.getNumber())
			.maxPage(memberPage.getTotalPages())
			.isLast(memberPage.isLast())
			.memberList(memberList)
			.build();
	}

	private ProfileResponseDto ToProfileResponseDto(MemberInfo member) {
		return ProfileResponseDto.builder()
			.userUuid(member.getUuid())
			.profileImage(member.getProfileImage())
			.nickname(member.getNickname())
			.favCategory(member.getFavoriteCategory())
			.build();
	}
}
