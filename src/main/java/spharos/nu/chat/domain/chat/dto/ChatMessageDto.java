package spharos.nu.chat.domain.chat.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageDto {
	@Schema(description = "이미지여부")
	private boolean hasImage;
	@Schema(description = "메시지")
	private String message;
	@Schema(description = "이미지url")
	private String imageUrl;
	@Schema(description = "채팅방id")
	private String chatRoomId;
}
