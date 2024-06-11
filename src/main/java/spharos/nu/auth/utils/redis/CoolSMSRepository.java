package spharos.nu.auth.utils.redis;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spharos.nu.auth.domain.auth.dto.request.VerificationDto;

@RequiredArgsConstructor
@Component
public class CoolSMSRepository {
	private final String PREFIX ="sms: ";
	private final StringRedisTemplate stringRedisTemplate;

	public void createSmsVerification(String phoneNumber, String verificationNumber) {
		int LIMIT_TIME = 3 * 60;
		stringRedisTemplate.opsForValue()
			.set(PREFIX + phoneNumber, verificationNumber, Duration.ofSeconds(LIMIT_TIME));
	}

	public String getSmsVerification(String phoneNumber) {
		return stringRedisTemplate.opsForValue().get(PREFIX + phoneNumber);
	}

	public void removeSmsVerification(String phoneNumber) {
		stringRedisTemplate.delete(PREFIX + phoneNumber);
	}

	public boolean hasKey(String phoneNumber) {
		return Boolean.TRUE.equals(stringRedisTemplate.hasKey(PREFIX + phoneNumber));
	}

	public boolean isVerified(VerificationDto verificationDto) {
		return getSmsVerification(verificationDto.getPhoneNumber())
				.equals(verificationDto.getVerificationNumber());
	}
}
