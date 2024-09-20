package spharos.nu.chat.domain.chat.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.chat.domain.chat.dto.event.WinningEventDto;
import spharos.nu.chat.domain.chat.dto.response.ChatRoomInfo;
import spharos.nu.chat.domain.chat.service.ChatRoomService;
import spharos.nu.chat.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/chat")
@Tag(name = "ChatRoom", description = "채팅방 컨트롤러")
public class ChatRoomController {

	private final ChatRoomService chatRoomService;

	@Operation(summary = "채팅방 목록 조회", description = "채팅방 목록을 조회합니다")
	@GetMapping(value = "/rooms/{userUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<List<ChatRoomInfo>>> getChatRoomsByMemberId(
		@PathVariable(value = "userUuid") String userUuid) {
		log.info("getChatRoomsByMemberId : {}", userUuid);
		return ApiResponse.success(chatRoomService.getChatRoomsByMemberId(userUuid),"채팅방 목록 조회 성공");
	}

	@Operation(summary = "채팅방 생성", description = "개발 편의용으로 채팅방 생성 시 사용합니다.(원래는 카프카로 서비스단 바로 호출)")
	@PostMapping(value = "/room/{userUuid}/for-dev")
	public ResponseEntity<ApiResponse<Void>> createChatRoom(@RequestBody WinningEventDto winningEventDto) {
		log.info("createChatRoom : {}", winningEventDto);
		chatRoomService.createChatRoom(winningEventDto);
		return ApiResponse.success(null, "채팅방 생성 성공");
	}

}
