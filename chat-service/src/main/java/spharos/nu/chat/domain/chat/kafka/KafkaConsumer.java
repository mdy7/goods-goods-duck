package spharos.nu.chat.domain.chat.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.chat.domain.chat.dto.event.WinningEventDto;
import spharos.nu.chat.domain.chat.service.ChatRoomService;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

	private final ChatRoomService chatRoomService;

	@KafkaListener(topics = "winning-bid-topic", containerFactory = "winningEventListener")
	public void tradingCompleteEventListener(WinningEventDto winningEventDto) {

		chatRoomService.createChatRoom(winningEventDto);
	}
}
