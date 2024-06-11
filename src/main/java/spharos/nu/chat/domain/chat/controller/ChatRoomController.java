package spharos.nu.chat.domain.chat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.chat.domain.chat.service.ChatRoomService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/chat-room")
@Tag(name = "ChatRoom", description = "채팅방 컨트롤러")
public class ChatRoomController {

	private final ChatRoomService chatRoomService;

}
