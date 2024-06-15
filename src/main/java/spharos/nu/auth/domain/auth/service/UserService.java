package spharos.nu.auth.domain.auth.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.auth.domain.auth.dto.event.JoinEventDto;
import spharos.nu.auth.domain.auth.dto.request.ResetPwdDto;
import spharos.nu.auth.domain.auth.dto.request.JoinDto;
import spharos.nu.auth.domain.auth.dto.request.LoginDto;
import spharos.nu.auth.domain.auth.dto.request.SocialLoginDto;
import spharos.nu.auth.domain.auth.dto.request.UpdatePwdDto;
import spharos.nu.auth.domain.auth.dto.request.WithdrawDto;
import spharos.nu.auth.domain.auth.dto.response.LoginResponseDto;
import spharos.nu.auth.domain.auth.entity.Member;
import spharos.nu.auth.domain.auth.entity.SocialMember;
import spharos.nu.auth.domain.auth.entity.WithdrawMember;
import spharos.nu.auth.domain.auth.repository.SocialRepository;
import spharos.nu.auth.domain.auth.repository.UserRepository;
import spharos.nu.auth.domain.auth.repository.WithdrawRepository;
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
	private final WithdrawRepository withdrawRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final KafkaTemplate<String, JoinEventDto> kafkaTemplate;

	public LoginResponseDto login(LoginDto loginDto) {
		Member member = userRepository.findByUserId(loginDto.getUserId())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
			throw new CustomException(ErrorCode.PASSWORD_ERROR);
		}

		Optional<WithdrawMember> withdrawMember = withdrawRepository.findByUuid(member.getUuid());

		if (withdrawMember.isPresent() && member.isWithdraw()) {
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime withdrawTime = withdrawMember.get().getCreatedAt();

			long daysBetween = ChronoUnit.DAYS.between(now.toLocalDate(), withdrawTime.toLocalDate());

			if (daysBetween <= 15) {
				member.changeWithdraw(false);
				withdrawRepository.delete(withdrawMember.get());
			}
			else {
				userRepository.delete(member);
			}
		}

		JwtToken jwtToken = jwtProvider.createToken(member.getUuid());

		return LoginResponseDto.builder()
			.uuid(member.getUuid())
			.accessToken(jwtToken.getAccessToken())
			.refreshToken(jwtToken.getRefreshToken())
			.build();
	}

	public LoginResponseDto socialLogin(SocialLoginDto socialLoginDto) {
		SocialMember social = socialRepository.findByMemberCode(socialLoginDto.getMemberCode())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		Member member = userRepository.findByUuid(social.getUuid())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		JwtToken jwtToken = jwtProvider.createToken(member.getUuid());

		return LoginResponseDto.builder()
			.uuid(member.getUuid())
			.accessToken(jwtToken.getAccessToken())
			.accessToken(jwtToken.getRefreshToken())
			.build();
	}

	public void join(JoinDto joinDto) {
		String uuid = String.valueOf(UUID.randomUUID());
		String encodedPassword = passwordEncoder.encode(joinDto.getPassword());
		Member member = Member.builder()
			.uuid(uuid)
			.userId(joinDto.getUserId())
			.password(encodedPassword)
			.phoneNumber(joinDto.getPhoneNumber())
			.build();
		userRepository.save(member);

		JoinEventDto kafka = JoinEventDto.builder()
			.uuid(uuid)
			.nickname(joinDto.getNickname())
			.profileImage(joinDto.getProfileImage())
			.favoriteCategory(joinDto.getFavoriteCategory())
			.build();
		kafkaTemplate.send("join-topic", kafka);
	}

	public void isDuplicatedId(String userId) {
		Optional<Member> isMember = userRepository.findByUserId(userId);
		if (isMember.isPresent()) {
			throw new CustomException(ErrorCode.ALREADY_EXIST_USER);
		}
	}

	public String findId(String phoneNumber) {
		Member member = userRepository.findByPhoneNumber(phoneNumber)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		return member.getUserId();
	}

	public void findPwd(String userId) {
		Member member = userRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
	}

	public void resetPwd(ResetPwdDto resetPwdDto) {
		Member member = userRepository.findByPhoneNumber(resetPwdDto.getPhoneNumber())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		if (passwordEncoder.matches(resetPwdDto.getNewPassword(), member.getPassword())) {
			throw new CustomException(ErrorCode.ALREADY_EXIST_PASSWORD);
		}

		String encodedNewPassword = passwordEncoder.encode(resetPwdDto.getNewPassword());
		resetPwdDto.resetPassword(member, encodedNewPassword);
		userRepository.save(member);
	}

	public void updatePwd(UpdatePwdDto updatePwdDto, String uuid) {
		Member member = userRepository.findByUuid(uuid)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		String encodedNewPassword = passwordEncoder.encode(updatePwdDto.getNewPassword());
		updatePwdDto.updatePassword(member, encodedNewPassword);
		userRepository.save(member);
	}

	public void withdraw(WithdrawDto withdrawDto, String uuid) {
		Member member = userRepository.findByUuid(uuid)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
		member.changeWithdraw(true);
		userRepository.save(member);

		WithdrawMember withdrawMember = WithdrawMember.builder()
			.uuid(uuid)
			.reason(withdrawDto.getReason())
			.build();
	}

	public LoginResponseDto cancelWithdraw(LoginDto loginDto) {
		Member member = userRepository.findByUserId(loginDto.getUserId())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		WithdrawMember withdrawMember = withdrawRepository.findByUuid(member.getUuid())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

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
}
