package spharos.nu.notification.domain.notification.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import spharos.nu.notification.domain.notification.entity.UserNotificationInfo;

import java.util.Optional;

@Repository
public interface UserNotificationInfoRepository extends MongoRepository<UserNotificationInfo, String> {
    Optional<UserNotificationInfo> findByUuid(String uuid);

    Optional<UserNotificationInfo> findByDeviceToken(String token);
}
