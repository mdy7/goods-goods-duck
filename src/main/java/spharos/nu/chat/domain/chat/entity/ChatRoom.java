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
@Document(collection = "chat_room")
public class ChatRoom {

	@Id
	@Field("chat_room_id")
	private String id;
	private List<String> participants;
	@Field("last_message")
	private String lastMessage;
	@Field("unread_count")
	private Integer unreadCount;
	@Field("goods_code")
	private String goodsCode;
	@CreatedDate
	@Field("created_at")
	private LocalDateTime createdAt;
	@CreatedDate
	@Field("updated_at")
	private LocalDateTime updatedAt;

}
