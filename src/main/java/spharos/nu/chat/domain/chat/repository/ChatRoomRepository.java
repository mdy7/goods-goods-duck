package spharos.nu.chat.domain.chat.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.chat.domain.chat.entity.ChatRoom;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

	@Query(value = "{ 'members': { $elemMatch: { 'memberName': ?0 } } }", sort = "{ 'updatedAt': -1 }")
	List<ChatRoom> getChatRoomsByMemberName(String memberName);
}
