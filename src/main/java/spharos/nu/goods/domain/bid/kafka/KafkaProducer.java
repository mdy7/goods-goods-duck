package spharos.nu.goods.domain.bid.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import spharos.nu.goods.domain.bid.dto.event.WinningEventDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, WinningEventDto> kafkaTemplate;

    public void sendWinningEvent(WinningEventDto winningEventDto) {
        kafkaTemplate.send("winning-bid-topic", winningEventDto);
    }
}
