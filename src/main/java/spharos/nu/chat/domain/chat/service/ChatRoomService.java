package spharos.nu.chat.domain.chat.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spharos.nu.chat.domain.chat.dto.event.WinningEventDto;
import spharos.nu.chat.domain.chat.entity.ChatRoom;
import spharos.nu.chat.domain.chat.entity.ChatRoom.ChatMember;
import spharos.nu.chat.domain.chat.repository.ChatRoomRepository;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;

	@Transactional
	@KafkaListener(topics = "winning-bid-topic", containerFactory = "winningEventListener")
	public void createChatRoom(WinningEventDto winningEventDto) {

		List<ChatMember> members = new ArrayList<>();
		members.add(ChatMember.builder()
			.userUuid(winningEventDto.getSellerUuid())
			.unreadCount(0)
			.build());
		members.add(ChatMember.builder()
			.userUuid(winningEventDto.getBidderUuid())
			.unreadCount(0)
			.build());

		chatRoomRepository.save(ChatRoom.builder()
			.members(members)
			.goodsCode(winningEventDto.getGoodsCode())
			.createdAt(LocalDateTime.now())
			.updatedAt(null)
			.build());
	}
}
