package spharos.nu.member.domain.member.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.member.domain.member.dto.response.AllMemberResponseDto;
import spharos.nu.member.domain.member.dto.response.ProfileResponseDto;
import spharos.nu.member.domain.member.entity.MemberInfo;
import spharos.nu.member.domain.member.repository.MemberInfoRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

	private final MemberInfoRepository memberInfoRepository;

	public AllMemberResponseDto allMemberGet(Pageable pageable) {

		Page<MemberInfo> memberPage = memberInfoRepository.findAll(pageable);

		List<ProfileResponseDto> memberList = memberPage.getContent().stream()
			.map(member -> ProfileResponseDto.builder()
				.userUuid(member.getUuid())
				.profileImg(member.getProfileImage())
				.nickname(member.getNickname())
				.favCategory(member.getFavoriteCategory())
				.build())
			.toList();

		return AllMemberResponseDto.builder()
			.totalCount(memberPage.getTotalElements())
			.nowPage(memberPage.getNumber())
			.maxPage(memberPage.getTotalPages())
			.isLast(memberPage.isLast())
			.memberList(memberList)
			.build();
	}
}
