package spharos.nu.chat.domain.chat.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import spharos.nu.chat.domain.chat.dto.ChatMessageDto;
import spharos.nu.chat.domain.chat.entity.ChatMessage;
import spharos.nu.chat.domain.chat.service.ChatService;
import spharos.nu.chat.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/chat")
@Tag(name = "Chat", description = "채팅 컨트롤러")
public class ChatController {

	private final ChatService chatService;

	@Operation(summary = "채팅 메시지 조회", description = "채팅 메시지를 조회합니다.")
	@GetMapping(value = "", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ChatMessage> getChatMessages(
		@RequestHeader(value = "User-Uuid", required = false) String uuid
	) {
		return chatService.getChatMessages().subscribeOn(Schedulers.boundedElastic());
	}

	@Operation(summary = "채팅 메시지 생성", description = "채팅 메시지를 생성합니다")
	@PostMapping()
	public Mono<ResponseEntity<ApiResponse<ChatMessage>>> createMessage(
		@RequestHeader(value = "User-Uuid", required = false) String uuid,
		@RequestBody ChatMessageDto chatMessageDto
	) {

		return chatService.createMessage(uuid, chatMessageDto)
			.map(message -> ApiResponse.success(message,"메시지 생성 성공"));
	}

	@Operation(summary = "메시지 읽음 상태 조회", description = "메시지의 읽음 상태를 조회합니다.")
	@GetMapping()
	public ResponseEntity<ApiResponse<Void>> getMessageIsRead(
		@RequestHeader(value = "User-Uuid", required = false) String uuid
	) {
		return null;
	}

}
