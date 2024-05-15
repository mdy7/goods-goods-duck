package spharos.nu.member.utils.redis;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import spharos.nu.member.domain.member.dto.VerificationDto;

@RequiredArgsConstructor
public class VerificationRepository {
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
