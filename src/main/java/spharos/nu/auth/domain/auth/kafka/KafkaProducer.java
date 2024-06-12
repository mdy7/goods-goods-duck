package spharos.nu.auth.domain.auth.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spharos.nu.auth.domain.auth.dto.event.JoinEventDto;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
	private final KafkaTemplate<String, JoinEventDto> kafkaTemplate;

	public void sendJoinEvent(JoinEventDto joinEventDto) {
		kafkaTemplate.send("join-topic", joinEventDto);
	}
}