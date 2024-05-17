package spharos.nu.member.domain.member.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.member.domain.member.dto.VerificationDto;
import spharos.nu.member.domain.member.entity.Member;
import spharos.nu.member.domain.member.repository.UserRepository;
import spharos.nu.member.global.exception.CustomException;
import spharos.nu.member.global.exception.errorcode.ErrorCode;
import spharos.nu.member.utils.redis.CoolSMSService;
import spharos.nu.member.utils.redis.CoolSMSRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationService {

	private final UserRepository userRepository;
	private final CoolSMSRepository coolSMSRepository;
	private final CoolSMSService coolSMSService;

	public void sendJoinSms(VerificationDto verificationDto) {
		log.info("진입");

		String to = verificationDto.getPhoneNumber();

		// 휴대폰 번호로 기존 회원 조회
		Optional<Member> isMember = userRepository.findByPhoneNumber(to);
		if (isMember.isPresent()) {
			throw new CustomException(ErrorCode.ALREADY_EXIST_USER);
		}

		// 인증번호는 100000부터 999999까지의 6자리 숫자
		int randomNumber = (int)(Math.random() * 1000000);
		String verificationNumber = String.format("%06d", randomNumber);
		// 인증번호 발송
		coolSMSService.sendSms(to, verificationNumber);
		// 인증번호 Redis 저장
		coolSMSRepository.createSmsVerification(to, verificationNumber);
	}

	public void verifySms(VerificationDto verificationDto) {
		if (!coolSMSRepository.hasKey(verificationDto.getPhoneNumber())) {
			throw new CustomException(ErrorCode.INVALID_REQUEST_METHOD);
		} else if (!coolSMSRepository.isVerified(verificationDto)) {
			throw new CustomException(ErrorCode.WRONG_NUMBER);
		}

		coolSMSRepository.removeSmsVerification(verificationDto.getPhoneNumber());
	}
}
