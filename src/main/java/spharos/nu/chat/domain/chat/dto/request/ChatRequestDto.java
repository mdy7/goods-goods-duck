package spharos.nu.chat.domain.chat.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRequestDto {

	@JsonProperty("isImage")
	// @Getter(AccessLevel.NONE)
	@Schema(description = "이미지여부")
	private Boolean isImage;
	@Schema(description = "메시지")
	private String message;
	@Schema(description = "이미지url")
	private String imageUrl;
	@Schema(description = "받는사람uuid")
	private String receiverUuid;
	@Schema(description = "채팅방id")
	private String chatRoomId;

}
