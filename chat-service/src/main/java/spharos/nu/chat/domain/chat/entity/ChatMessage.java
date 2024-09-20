package spharos.nu.chat.domain.chat.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
	private Boolean isImage;
	private String message;
	private String imageUrl;
	private String senderUuid;
	private String receiverUuid;
	private Boolean isRead;
	@CreatedDate
	private LocalDateTime createdAt;
	private String chatRoomId;
	private Boolean inOut;

}
