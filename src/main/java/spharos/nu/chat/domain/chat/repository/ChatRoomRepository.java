package spharos.nu.chat.domain.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;
import spharos.nu.chat.domain.chat.entity.ChatRoom;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

	Optional<ChatRoom> findByGoodsCode(String goodsCode);

	@Query(value = "{ 'members': { $elemMatch: { 'userUuid': ?0 } } }", sort = "{ 'updatedAt': -1 }")
	List<ChatRoom> getChatRoomsByUserUuid(String userUuid);
}
