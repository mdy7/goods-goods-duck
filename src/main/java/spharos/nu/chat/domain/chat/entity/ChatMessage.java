package spharos.nu.chat.domain.chat.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "chat_message")
public class ChatMessage {

	@Id
	private String id;
	@Field("has_image")
	private Boolean isImage;
	private String message;
	@Field("image_url")
	private String imageUrl;
	@Field("sender_uuid")
	private String senderUuid;
	@Field("receiver_uuid")
	private String receiverUuid;
	@Field("is_read")
	private boolean isRead;
	@CreatedDate
	@Field("created_at")
	private LocalDateTime createdAt;
	@Field("chat_room_id")
	private String chatRoomId;

}
