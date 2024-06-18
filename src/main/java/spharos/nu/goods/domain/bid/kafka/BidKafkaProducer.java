package spharos.nu.goods.domain.bid.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import spharos.nu.goods.domain.bid.dto.event.WinningEventDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class BidKafkaProducer {

    private final KafkaTemplate<String, WinningEventDto> winningKafkaTemplate;

    public void sendWinningEvent(WinningEventDto winningEventDto) {
        winningKafkaTemplate.send("winning-bid-topic", winningEventDto);
    }
}
