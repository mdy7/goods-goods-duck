package spharos.nu.member.utils.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

import jakarta.annotation.PostConstruct;

@Component
public class CoolSMSService {
	@Value("${coolSms.key}")
	private String apiKey;
	@Value("${coolSms.secret}")
	private String secretKey;
	@Value("${coolSms.number}")
	private String phoneNumber;

	DefaultMessageService messageService;

	@PostConstruct
	public void init() {
		this.messageService = NurigoApp.INSTANCE.initialize(apiKey, secretKey, "https://api.coolsms.co.kr");
	}

	public SingleMessageSentResponse sendSms(String to, String verificationNumber) {
		Message message = new Message();
		message.setFrom(phoneNumber);
		message.setTo(to);
		message.setText("[goods-goods-duck] 인증번호 [" + verificationNumber + "] 를 입력하세요.");

		return this.messageService.sendOne(new SingleMessageSendingRequest(message));
	}
}
