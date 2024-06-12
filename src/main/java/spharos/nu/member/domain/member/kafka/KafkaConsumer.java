package spharos.nu.member.domain.member.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spharos.nu.member.domain.member.dto.event.JoinEventDto;
import spharos.nu.member.domain.member.service.MyPageService;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

	private final MyPageService myPageService;

	@KafkaListener(topics = "join-topic", containerFactory = "joinEventListener")
	public void joinEventListener(JoinEventDto joinEventDto) {
		myPageService.join(joinEventDto);
	}
}
