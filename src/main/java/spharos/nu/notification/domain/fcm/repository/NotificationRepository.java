package spharos.nu.notification.domain.fcm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<spharos.nu.notification.domain.fcm.entity.Notification, Long> {
}
