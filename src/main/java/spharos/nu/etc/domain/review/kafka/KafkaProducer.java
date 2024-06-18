package spharos.nu.etc.domain.review.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.review.dto.event.MemberReviewEventDto;
import spharos.nu.etc.domain.review.dto.event.TradingCompleteEventDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

	private final KafkaTemplate<String, MemberReviewEventDto> memberKafkaTemplate;
	private final KafkaTemplate<String, TradingCompleteEventDto> statusKafkaTemplate;

	// 회원 점수 카프카
	public void sendMemberScore(MemberReviewEventDto memberReviewEventDto) {

		memberKafkaTemplate.send("member-score-topic", memberReviewEventDto);
	}

	// 거래 상태 카프카
	public void sendTradingStatus(TradingCompleteEventDto tradingCompleteEventDto) {

		statusKafkaTemplate.send("trading-complete-topic", tradingCompleteEventDto);
	}
}
