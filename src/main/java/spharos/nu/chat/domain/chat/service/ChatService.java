package spharos.nu.chat.domain.chat.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.bson.Document;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.client.model.changestream.OperationType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spharos.nu.chat.domain.chat.dto.request.ChatRequestDto;
import spharos.nu.chat.domain.chat.dto.response.ChatResposeDto;
import spharos.nu.chat.domain.chat.entity.ChatMessage;
import spharos.nu.chat.domain.chat.entity.ChatRoom;
import spharos.nu.chat.domain.chat.entity.ChatRoom.ChatMember;
import spharos.nu.chat.domain.chat.repository.ChatMessageRepository;
import spharos.nu.chat.domain.chat.repository.ChatMessageRestRepository;
import spharos.nu.chat.domain.chat.repository.ChatRoomRepository;
import spharos.nu.chat.global.exception.CustomException;
import spharos.nu.chat.global.exception.errorcode.ErrorCode;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

	private final ChatMessageRepository chatMessageRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRestRepository chatMessageRestRepository;
	private final ReactiveMongoTemplate reactiveMongoTemplate;

	@Transactional
	public Mono<ChatMessage> createMessage(String senderUuid, ChatRequestDto chatRequestDto) {
		log.info("채팅방 아이디" + chatRequestDto.getChatRoomId());
		ChatRoom chatRoom = chatRoomRepository.findById(chatRequestDto.getChatRoomId()).orElseThrow(
			() -> new CustomException(ErrorCode.NOT_FOUND_CHAT_ROOM)
		);

		List<ChatMember> members = chatRoom.getMembers();
		members.stream().peek(
			member -> {
				if (!member.getUserUuid().equals(senderUuid)) {
					member.setUnreadCount(member.getUnreadCount() + 1);
				}
			}
		).forEach(member -> log.info("member: {}", member));
        log.info("chatRoom members의 unreadCount 업데이트");
		chatRoomRepository.save(ChatRoom.builder()
			.id(chatRoom.getId())
			.goodsCode(chatRoom.getGoodsCode())
			.createdAt(chatRoom.getCreatedAt())
			.members(members)
			.updatedAt(LocalDateTime.now())
			.build());

		return chatMessageRepository.save(
			ChatMessage.builder()
				.senderUuid(senderUuid)
				.receiverUuid(chatRequestDto.getReceiverUuid())
				.isRead(false)
				.chatRoomId(chatRequestDto.getChatRoomId())
				.createdAt(LocalDateTime.now())
				.isImage(chatRequestDto.getIsImage())
				.imageUrl(chatRequestDto.getIsImage() ? chatRequestDto.getImageUrl() : null)
				.message(chatRequestDto.getIsImage() ? null : chatRequestDto.getMessage())
				.build()
		);
	}

	public Flux<ChatResposeDto> getChatMessages(String chatRoomId) {
		log.info("getChatMessages 메서드로 채팅메시지 내역을 조회");
		return chatMessageRepository.findChatMessageByChatRoomId(chatRoomId)
			.map(chatMessage -> ChatResposeDto.builder()
				.chatMessageId(chatMessage.getId())
				.chatRoomId(chatMessage.getChatRoomId())
				.senderUuid(chatMessage.getSenderUuid())
				.receiverUuid(chatMessage.getReceiverUuid())
				.isImage(chatMessage.getIsImage())
				.message(chatMessage.getMessage())
				.imageUrl(chatMessage.getImageUrl())
				.createdAt(chatMessage.getCreatedAt())
				.build());
	}

	public ChatResposeDto getLastMessage(String chatRoomId) {
		ChatMessage chatMessage = chatMessageRestRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(chatRoomId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHAT_ROOM));
		return ChatResposeDto.builder()
			.chatMessageId(chatMessage.getId())
			.chatRoomId(chatMessage.getChatRoomId())
			.senderUuid(chatMessage.getSenderUuid())
			.receiverUuid(chatMessage.getReceiverUuid())
			.isImage(chatMessage.getIsImage())
			.message(chatMessage.getMessage())
			.imageUrl(chatMessage.getImageUrl())
			.createdAt(chatMessage.getCreatedAt())
			.build();
	}

	public Flux<ChatResposeDto> getNewMessage(String chatRoomId) {
		ChangeStreamOptions options = ChangeStreamOptions.builder()
			.filter(Aggregation.newAggregation(
				Aggregation.match(Criteria.where("operationType").is(OperationType.INSERT.getValue())),
				Aggregation.match(Criteria.where("fullDocument.roomId").is(chatRoomId))
			))
			.build();

		return reactiveMongoTemplate.changeStream("chat", options, Document.class)
			.map(ChangeStreamEvent::getBody)
			.map(document -> ChatResposeDto.builder()
				.chatMessageId(document.getObjectId("_id").toString())
				.chatRoomId(document.getString("chatRoomId"))
				.senderUuid(document.getString("senderUuid"))
				.receiverUuid(document.getString("receiverUuid"))
				.isImage(document.getBoolean("isImage"))
				.message(document.getString("message"))
				.imageUrl(document.getString("imageUrl"))
				.createdAt(LocalDateTime.ofInstant(document.getDate("createdAt").toInstant(), ZoneId.systemDefault()))
				.build());
	}

    /* 메시지 읽음 상태 조회 추후 구현
	public Flux<ChatMessage> getMessageIsRead(String chatRoomId) {
		return chatMessageRepository.findChatMessageByChatRoomId(chatRoomId);
	}
    */
}
