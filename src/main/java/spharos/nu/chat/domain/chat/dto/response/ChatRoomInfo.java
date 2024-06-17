package spharos.nu.chat.domain.chat.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import spharos.nu.chat.domain.chat.entity.ChatRoom.ChatMember;

@Getter
@Builder
public class ChatRoomInfo {
	@Schema(description = "채팅방 id")
	private String chatRoomId;
	@Schema(description = "굿즈 코드")
	private String goodsCode;
	@Schema(description = "채팅방 참여자 목록")
	private List<ChatMember> members;
	@Schema(description = "채팅방 업데이트 시간")
	private LocalDateTime updatedAt;
}
