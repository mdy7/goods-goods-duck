package spharos.nu.auth.domain.auth.kafka.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spharos.nu.auth.domain.auth.kafka.dto.JoinResponseDto;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

	private final KafkaTemplate<String, JoinResponseDto> kafkaTemplate;

	public void sendJoinEvent(JoinResponseDto joinResponseDto) {
		kafkaTemplate.send("join-event", joinResponseDto);
	}
}