package spharos.nu.chat.domain.chat.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
		log.info(chatRequestDto.getChatRoomId() + " 번 채팅방에 메시지 생성");
		ChatRoom chatRoom = chatRoomRepository.findById(chatRequestDto.getChatRoomId()).orElseThrow(
			() -> new CustomException(ErrorCode.NOT_FOUND_CHAT_ROOM)
		);

		List<ChatMember> members = chatRoom.getMembers();
		/* 읽음 여부를 결정 짓는 조건문 */
		Boolean isRead = members.stream().anyMatch(
			member -> {
				/* request 가 입장-퇴장을 알려주는 메시지일 때 */
				if (!chatRequestDto.getInOut().isEmpty()) {
					if (chatRequestDto.getInOut().equals("in") && member.getUserUuid().equals(senderUuid)) { //채팅방 입장
						member.setUnreadCount(0); // 안읽은 메시지 초기화
						member.setConnect(true);  // connect 상태 true
						return false;
					} else if (chatRequestDto.getInOut().equals("out") && member.getUserUuid().equals(senderUuid)) { //채팅방 퇴장
						member.setUnreadCount(0);  // 안읽은 메시지 초기화(필수x 확실하게 하는 장치)
						member.setConnect(false);  // connect 상태 off
						return false;
					}
				/* request 가 일반적인 메시지일 때 */
				} else if (!member.getUserUuid().equals(senderUuid)) {
					if (!member.getConnect()) {
						member.setUnreadCount(member.getUnreadCount() + 1); //대화 상대의 안읽은 메시지 수를 +1
						return false;
					} else {
						return true;
					}
				}
				return false;
			});

		chatRoomRepository.save(ChatRoom.builder()
			.id(chatRoom.getId())
			.goodsCode(chatRoom.getGoodsCode())
			.createdAt(chatRoom.getCreatedAt())
			.members(members)
			.updatedAt(LocalDateTime.now())
			.build());
		log.info("chatRoom members 정보를 업데이트 후 새 메시지 생성");
		return chatMessageRepository.save(
			ChatMessage.builder()
				.senderUuid(senderUuid)
				.receiverUuid(chatRequestDto.getReceiverUuid())
				.isRead(isRead)
				.chatRoomId(chatRequestDto.getChatRoomId())
				.createdAt(LocalDateTime.now())
				.isImage(chatRequestDto.getInOut().isEmpty() ? chatRequestDto.getIsImage() : null)
				.imageUrl(chatRequestDto.getInOut().isEmpty() ? chatRequestDto.getIsImage() ? chatRequestDto.getImageUrl() : null : null)
				.message(chatRequestDto.getInOut().isEmpty() ? chatRequestDto.getIsImage() ? null : chatRequestDto.getMessage() : null)
				.build()
		);
	}

	public Flux<ChatResposeDto> getChatMessages(String chatRoomId) {
		log.info("getChatMessages 메서드로 채팅메시지 내역을 조회");
		log.info("chatRoomId : {}", chatRoomId);

		Flux<ChatResposeDto> test = chatMessageRepository.findChatMessageByChatRoomId(chatRoomId)
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

		log.info(" 반환값 "+test);

		return test;
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
		log.info("새로운 메시지 하나를 조회합니다");
		ChangeStreamOptions options = ChangeStreamOptions.builder()
			.filter(Aggregation.newAggregation(
				Aggregation.match(Criteria.where("operationType").is(OperationType.INSERT.getValue())),
				Aggregation.match(Criteria.where("fullDocument.chatRoomId").is(chatRoomId))
				// Aggregation.match(Criteria.where("fullDocument.inOut").is(""))
			))
			.build();
		log.info("옵션 생성 완료");

		Flux<ChatResposeDto> newDto = reactiveMongoTemplate.changeStream("chat_message", options, Document.class)
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

		log.info("newDto"+newDto);

		return newDto;
	}

    /* 메시지 읽음 상태 조회 추후 구현
	public Flux<ChatMessage> getMessageIsRead(String chatRoomId) {
		return chatMessageRepository.findChatMessageByChatRoomId(chatRoomId);
	}
    */
}
