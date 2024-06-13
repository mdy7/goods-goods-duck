package spharos.nu.chat.domain.chat.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatResposeDto {
	@Schema(description = "채팅 메시지 id")
	private String chatMessageId;
	@Schema(description = "채팅방 id")
	private String chatRoomId;
	@JsonProperty("isImage")
	@Getter(AccessLevel.NONE)
	@Schema(description = "이미지여부")
	private Boolean isImage;
	@Schema(description = "메시지")
	private String message;
	@Schema(description = "이미지 url")
	private String imageUrl;
	@Schema(description = "보낸사람 uuid")
	private String senderUuid;
	@Schema(description = "받는사람 uuid")
	private String receiverUuid;
	@Schema(description = "채팅 보낸 시간")
	private LocalDateTime createdAt;
}
