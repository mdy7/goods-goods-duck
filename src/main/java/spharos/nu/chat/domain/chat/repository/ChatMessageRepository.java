package spharos.nu.chat.domain.chat.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spharos.nu.chat.domain.chat.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, String> {

	@Tailable
	@Query("{'chatRoomId': ?0}")
	Flux<ChatMessage> findChatMessageByChatRoomId(String chatRoomId);
}
