package spharos.nu.notification.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spharos.nu.notification.domain.notification.entity.UserNotificationInfo;

import java.util.Optional;

@Repository
public interface UserNotificationInfoRepository extends JpaRepository<UserNotificationInfo, Long> {
    Optional<UserNotificationInfo> findByUuid(String uuid);
}
