package spharos.nu.member.domain.member.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.member.domain.member.dto.JoinDto;
import spharos.nu.member.domain.member.dto.LoginDto;
import spharos.nu.member.domain.member.dto.SocialLoginDto;
import spharos.nu.member.domain.member.entity.Member;
import spharos.nu.member.domain.member.entity.SocialMember;
import spharos.nu.member.domain.member.repository.SocialRepository;
import spharos.nu.member.domain.member.repository.UserRepository;
import spharos.nu.member.global.exception.CustomException;
import spharos.nu.member.global.exception.errorcode.ErrorCode;
import spharos.nu.member.utils.jwt.JwtProvider;
import spharos.nu.member.utils.jwt.JwtToken;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
	private final UserRepository userRepository;
	private final SocialRepository socialRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	public JwtToken login(LoginDto loginDto) {
		Member member = userRepository.findByUserId(loginDto.getUserId())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
			throw new CustomException(ErrorCode.PASSWORD_ERROR);
		}

		return jwtProvider.createToken(member.getUuid());
	}

	public JwtToken socialLogin(SocialLoginDto socialLoginDto) {
		SocialMember social = socialRepository.findByMemberCode(socialLoginDto.getMemberCode())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		Member member = userRepository.findByUuid(social.getUuid())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		return jwtProvider.createToken(member.getUuid());
	}

	public void join(JoinDto joinDto) {
		String uuid = String.valueOf(UUID.randomUUID());
		String encodedPassword = passwordEncoder.encode(joinDto.getPassword());

		log.info(uuid, encodedPassword, joinDto.getPassword());

		Member member = joinDto.toEntity(uuid, encodedPassword);
		userRepository.save(member);
	}

	public void isDuplicatedId(String userId) {
		Optional<Member> isMember = userRepository.findByUserId(userId);
		if (isMember.isPresent()) {
			throw new CustomException(ErrorCode.ALREADY_EXIST_USER);
		}
	}

	public void isDuplicatedNick(String nickname) {
		Optional<Member> isMember = userRepository.findByNickname(nickname);
		if (isMember.isPresent()) {
			throw new CustomException(ErrorCode.ALREADY_EXIST_USER);
		}
	}

	public String findId(String nickname) {
		Member member = userRepository.findByNickname(nickname)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		return member.getUserId();
	}

	public void findPw(String userId) {
		Member member = userRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
	}
}
