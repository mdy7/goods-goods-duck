package spharos.nu.chat.domain.chat.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.chat.domain.chat.entity.ChatMessage;

@Repository
public interface ChatMessageRestRepository extends MongoRepository<ChatMessage, String> {

	Optional<ChatMessage> findFirstByChatRoomIdAndInOutIsEmptyOrderByCreatedAtDesc(String roomId);
}
