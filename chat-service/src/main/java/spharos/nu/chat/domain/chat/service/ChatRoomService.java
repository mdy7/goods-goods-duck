package spharos.nu.chat.domain.chat.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.chat.domain.chat.dto.event.WinningEventDto;
import spharos.nu.chat.domain.chat.dto.response.ChatRoomInfo;
import spharos.nu.chat.domain.chat.entity.ChatRoom;
import spharos.nu.chat.domain.chat.entity.ChatRoom.ChatMember;
import spharos.nu.chat.domain.chat.repository.ChatRoomRepository;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;

	@Transactional
	public void createChatRoom(WinningEventDto winningEventDto) {
        log.info("낙찰 완료 & 채팅방을 생성합니다");
		log.info("입찰자 {}",winningEventDto.getBidderUuid());
		log.info("판매자 {}",winningEventDto.getSellerUuid());
		List<ChatMember> members = new ArrayList<>();
		members.add(ChatMember.builder()
			.userUuid(winningEventDto.getSellerUuid())
			.unreadCount(0)
			.connect(false)
			.build());
		members.add(ChatMember.builder()
			.userUuid(winningEventDto.getBidderUuid())
			.unreadCount(0)
			.connect(false)
			.build());

		chatRoomRepository.save(ChatRoom.builder()
			.members(members)
			.goodsCode(winningEventDto.getGoodsCode())
			.createdAt(LocalDateTime.now())
			.updatedAt(null)
			.build());
	}

	@Transactional
	public List<ChatRoomInfo> getChatRoomsByMemberId(String userUuid) {
        log.info("채팅방 목록 찾는 메서드 발동");
		List<ChatRoom> chatRooms = chatRoomRepository.getChatRoomsByUserUuid(userUuid);
		List<ChatRoomInfo> chatRoomResList = new ArrayList<>();
		log.info("채팅방 목록을 생성합니다");
		chatRooms.forEach(chatRoom -> {
			chatRoomResList.add(ChatRoomInfo.builder()
				.chatRoomId(chatRoom.getId())
				.members(chatRoom.getMembers())
				.goodsCode(chatRoom.getGoodsCode())
				.updatedAt(chatRoom.getUpdatedAt())
				.build());
		});
		return chatRoomResList;
	}
}
