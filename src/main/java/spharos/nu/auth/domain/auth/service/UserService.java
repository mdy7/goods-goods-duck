package spharos.nu.auth.domain.auth.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.auth.domain.auth.dto.ChangePwdDto;
import spharos.nu.auth.domain.auth.dto.JoinDto;
import spharos.nu.auth.domain.auth.dto.LoginDto;
import spharos.nu.auth.domain.auth.dto.LoginResponseDto;
import spharos.nu.auth.domain.auth.dto.SocialLoginDto;
import spharos.nu.auth.domain.auth.entity.Member;
import spharos.nu.auth.domain.auth.entity.SocialMember;
import spharos.nu.auth.domain.auth.repository.SocialRepository;
import spharos.nu.auth.domain.auth.repository.UserRepository;
import spharos.nu.auth.global.exception.CustomException;
import spharos.nu.auth.global.exception.errorcode.ErrorCode;
import spharos.nu.auth.utils.jwt.JwtProvider;
import spharos.nu.auth.utils.jwt.JwtToken;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
	private final UserRepository userRepository;
	private final SocialRepository socialRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	public LoginResponseDto login(LoginDto loginDto) {
		Member member = userRepository.findByUserId(loginDto.getUserId())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
			throw new CustomException(ErrorCode.PASSWORD_ERROR);
		}

		JwtToken jwtToken = jwtProvider.createToken(member.getUuid());

		return LoginResponseDto.builder()
			.jwtToken(jwtToken)
			.nickname(member.getNickname())
			.profileImage(member.getProfileImage())
			.favoriteCategory(member.getFavoriteCategory())
			.build();
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

		Member member = joinDto.toEntity(uuid, encodedPassword);
		userRepository.save(member);

		// Todo: 카프카로 2개의 추가 엔티티 생성 필요
		// MemberScore score = MemberScore.builder()
		// 	.uuid(uuid)
		// 	.score(50)
		// 	.build();
		// scoreRepository.save(score);
		//
		// DuckPoint point = DuckPoint.builder()
		// 	.uuid(uuid)
		// 	.nowPoint(0L)
		// 	.build();
		// pointRepository.save(point);
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

	public void findPwd(String userId) {
		Member member = userRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
	}

	public void changePwd(ChangePwdDto changePwdDto) {
		Member member = userRepository.findByPhoneNumber(changePwdDto.getPhoneNumber())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		if (passwordEncoder.matches(changePwdDto.getNewPassword(), member.getPassword())) {
			throw new CustomException(ErrorCode.ALREADY_EXIST_PASSWORD);
		}

		String encodedNewPassword = passwordEncoder.encode(changePwdDto.getNewPassword());
		changePwdDto.updatePassword(member, encodedNewPassword);
		userRepository.save(member);
	}

	public void withdraw(String uuid) {
		Member member = userRepository.findByUuid(uuid)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
		member.changeWithdraw(true, LocalDateTime.now());
		userRepository.save(member);
	}
}
