package spharos.nu.chat.domain.chat.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spharos.nu.chat.domain.chat.dto.ChatMessageDto;
import spharos.nu.chat.domain.chat.entity.ChatMessage;
import spharos.nu.chat.domain.chat.entity.Test;
import spharos.nu.chat.domain.chat.repository.ChatMessageRepository;
import spharos.nu.chat.domain.chat.repository.ChatRoomRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
    private final TestRepository testRepository;

	public void getChatRoomList() {

	}

	public Flux<ChatMessage> getChatMessages() {
		return chatMessageRepository.findAll();
	}

	@Transactional
	public Mono<ChatMessage> createMessage(String senderUuid, ChatMessageDto chatMessageDto) {
		ChatMessage chatMessage;
		LocalDateTime createdAt = LocalDateTime.now();
		if (chatMessageDto.isHasImage()) {
			chatMessage = ChatMessage.builder()
				.imageUrl(chatMessageDto.getImageUrl())
				.senderUuid(senderUuid)
				.isRead(false)
				.chatRoomId(chatMessageDto.getChatRoomId())
				.createdAt(createdAt)
				.build();
		} else {
			chatMessage = ChatMessage.builder()
				.message(chatMessageDto.getMessage())
				.senderUuid(senderUuid)
				.isRead(false)
				.chatRoomId(chatMessageDto.getChatRoomId())
				.createdAt(createdAt)
				.build();
		}

		return chatMessageRepository.save(chatMessage);
	}

	public Flux<ChatMessage> getMessageIsRead(String chatRoomId) {
		return chatMessageRepository.findChatMessageByChatRoomId(chatRoomId);
	}

}
