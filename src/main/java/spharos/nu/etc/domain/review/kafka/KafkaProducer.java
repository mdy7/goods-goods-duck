package spharos.nu.etc.domain.review.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.review.dto.event.MemberScoreEventDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

	private final KafkaTemplate<String, MemberScoreEventDto> memberKafkaTemplate;

	public void sendMemberScore(MemberScoreEventDto memberScoreEventDto) {

		memberKafkaTemplate.send("member-score-topic", memberScoreEventDto);
	}
}
