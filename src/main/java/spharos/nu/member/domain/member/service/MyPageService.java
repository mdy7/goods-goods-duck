package spharos.nu.member.domain.member.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.member.domain.member.dto.ProfileResponseDto;
import spharos.nu.member.domain.member.entity.Member;
import spharos.nu.member.domain.member.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

	private final UserRepository userRepository;

	public ProfileResponseDto profileGet(String uuid) {

		Member member = userRepository.findByUuid(uuid).orElseThrow();

		return ProfileResponseDto.builder()
			.profileImg(member.getProfileImage())
			.nickname(member.getNickname())
			.favCategory(member.getFavoriteCategory())
			.build();
	}
}
