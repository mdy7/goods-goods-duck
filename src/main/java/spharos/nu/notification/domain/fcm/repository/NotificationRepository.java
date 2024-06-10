package spharos.nu.notification.domain.fcm.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spharos.nu.notification.domain.fcm.dto.response.NotificationInfoDto;
import spharos.nu.notification.domain.fcm.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<NotificationInfoDto> findByUuid(String uuid, Pageable pageable);
}
