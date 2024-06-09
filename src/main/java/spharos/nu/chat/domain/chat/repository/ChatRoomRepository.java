package spharos.nu.chat.domain.chat.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.chat.domain.chat.entity.ChatRoom;

@Repository
public interface ChatRoomRepository extends ReactiveMongoRepository<ChatRoom, String> {

}
