package spharos.nu.auth.domain.auth.service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.auth.domain.auth.kafka.dto.JoinResponseDto;
import spharos.nu.auth.domain.auth.dto.request.ResetPwdDto;
import spharos.nu.auth.domain.auth.dto.request.JoinDto;
import spharos.nu.auth.domain.auth.dto.request.LoginDto;
import spharos.nu.auth.domain.auth.dto.request.SocialLoginDto;
import spharos.nu.auth.domain.auth.dto.request.UpdatePwdDto;
import spharos.nu.auth.domain.auth.dto.request.VerificationDto;
import spharos.nu.auth.domain.auth.dto.request.WithdrawDto;
import spharos.nu.auth.domain.auth.dto.response.LoginResponseDto;
import spharos.nu.auth.domain.auth.entity.Member;
import spharos.nu.auth.domain.auth.entity.SocialMember;
import spharos.nu.auth.domain.auth.entity.WithdrawMember;
import spharos.nu.auth.domain.auth.kafka.service.KafkaProducer;
import spharos.nu.auth.domain.auth.repository.SocialRepository;
import spharos.nu.auth.domain.auth.repository.UserRepository;
import spharos.nu.auth.domain.auth.repository.WithdrawRepository;
import spharos.nu.auth.global.exception.CustomException;
import spharos.nu.auth.utils.jwt.JwtProvider;
import spharos.nu.auth.utils.jwt.JwtToken;

import static spharos.nu.auth.global.exception.errorcode.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
	private final UserRepository userRepository;
	private final SocialRepository socialRepository;
	private final WithdrawRepository withdrawRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final KafkaProducer kafkaProducer;

	/**
	 * 로그인
	 */
	public LoginResponseDto login(LoginDto loginDto) {
		Member member = userRepository.findByUserId(loginDto.getUserId())
			.orElseThrow(() -> new CustomException(LOGIN_FAILED));

		validateWithdrawStatus(member);

		if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
			throw new CustomException(LOGIN_FAILED);
		}

		JwtToken jwtToken = jwtProvider.createToken(member.getUuid());

		return LoginResponseDto.builder()
			.uuid(member.getUuid())
			.accessToken(jwtToken.getAccessToken())
			.refreshToken(jwtToken.getRefreshToken())
			.build();
	}

	private void validateWithdrawStatus(Member member) {
		if (member.isWithdraw()) {
			throw new CustomException(LOGIN_FAILED);
		}
	}

	/**
	 * 소셜 로그인
	 */
	public LoginResponseDto socialLogin(SocialLoginDto socialLoginDto) {
		SocialMember social = socialRepository.findByMemberCode(socialLoginDto.getMemberCode())
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		Member member = userRepository.findByUuid(social.getUuid())
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		validateWithdrawStatus(member);

		JwtToken jwtToken = jwtProvider.createToken(member.getUuid());

		return LoginResponseDto.builder()
			.uuid(member.getUuid())
			.accessToken(jwtToken.getAccessToken())
			.refreshToken(jwtToken.getRefreshToken())
			.build();
	}

	/**
	 * 회원가입
	 */
	public void join(JoinDto joinDto) {
		String uuid = String.valueOf(UUID.randomUUID());

		if(userRepository.findByUserId(joinDto.getUserId()).isPresent()) {
			throw new CustomException(ALREADY_EXIST_USER);
		}

		userRepository.save(Member.builder()
				.uuid(uuid)
				.userId(joinDto.getUserId())
				.password(passwordEncoder.encode(joinDto.getPassword()))
				.phoneNumber(joinDto.getPhoneNumber())
				.build());

		kafkaProducer.sendJoinEvent(JoinResponseDto.builder()
				.uuid(uuid)
				.nickname(joinDto.getNickname())
				.profileImage(joinDto.getProfileImage())
				.favoriteCategory(joinDto.getFavoriteCategory())
				.build());


//		if (Objects.equals("kakao", joinDto.getPassword())) {
//			SocialMember socialMember = SocialMember.builder()
//				.uuid(uuid)
//				.memberCode(joinDto.getUserId())
//				.socialMemberType((byte)1)
//				.build();
//			socialRepository.save(socialMember);
//		}
	}

	public void socialMapping(VerificationDto verificationDto) {
		Optional<Member> isMember = userRepository.findByPhoneNumber(verificationDto.getPhoneNumber());
		if (isMember.isPresent()) {
			if (Objects.equals(verificationDto.getProvider(), "kakao")) {
				SocialMember socialMember = SocialMember.builder()
					.uuid(isMember.get().getUuid())
					.memberCode(verificationDto.getMemberCode())
					.socialMemberType((byte)1)
					.build();
				socialRepository.save(socialMember);
			}
		}
	}

	public void isDuplicatedId(String userId) {
		Optional<Member> isMember = userRepository.findByUserId(userId);
		if (isMember.isPresent()) {
			throw new CustomException(ALREADY_EXIST_USER);
		}
	}

	public String findId(String phoneNumber) {
		Member member = userRepository.findByPhoneNumber(phoneNumber)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		return member.getUserId();
	}

	public void findPwd(String userId) {
		Member member = userRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));
	}

	public void resetPwd(ResetPwdDto resetPwdDto) {
		Member member = userRepository.findByPhoneNumber(resetPwdDto.getPhoneNumber())
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		if (passwordEncoder.matches(resetPwdDto.getNewPassword(), member.getPassword())) {
			throw new CustomException(ALREADY_EXIST_PASSWORD);
		}

		String encodedNewPassword = passwordEncoder.encode(resetPwdDto.getNewPassword());
		resetPwdDto.resetPassword(member, encodedNewPassword);
		userRepository.save(member);
	}

	public void updatePwd(UpdatePwdDto updatePwdDto, String uuid) {
		Member member = userRepository.findByUuid(uuid)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		if (passwordEncoder.matches(updatePwdDto.getNewPassword(), member.getPassword())) {
			throw new CustomException(ALREADY_EXIST_PASSWORD);
		}

		String encodedNewPassword = passwordEncoder.encode(updatePwdDto.getNewPassword());
		updatePwdDto.updatePassword(member, encodedNewPassword);
		userRepository.save(member);
	}

	public void withdraw(WithdrawDto withdrawDto, String uuid) {
		Member member = userRepository.findByUuid(uuid)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));
		member.changeWithdraw(true);
		userRepository.save(member);

		WithdrawMember withdrawMember = WithdrawMember.builder()
			.uuid(uuid)
			.reason(withdrawDto.getReason())
			.build();

		withdrawRepository.save(withdrawMember);
	}

	public LoginResponseDto cancelWithdraw(LoginDto loginDto) {
		Member member = userRepository.findByUserId(loginDto.getUserId())
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		WithdrawMember withdrawMember = withdrawRepository.findByUuid(member.getUuid())
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		JwtToken jwtToken = jwtProvider.createToken(member.getUuid());

		member.changeWithdraw(false);
		userRepository.save(member);

		withdrawRepository.delete(withdrawMember);

		return LoginResponseDto.builder()
			.uuid(member.getUuid())
			.accessToken(jwtToken.getAccessToken())
			.refreshToken(jwtToken.getRefreshToken())
			.build();
	}


	/**
	 * 회원가입 실패 시 롤백 처리
	 */
	public void failJoin(String uuid) {
		userRepository.deleteByUuid(uuid);
	}
}
