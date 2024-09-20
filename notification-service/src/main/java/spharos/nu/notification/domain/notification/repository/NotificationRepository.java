package spharos.nu.notification.domain.notification.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import spharos.nu.notification.domain.notification.dto.response.NotificationInfoDto;
import spharos.nu.notification.domain.notification.entity.Notification;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    Page<NotificationInfoDto> findByUuid(String uuid, Pageable pageable);

    Long countByUuidAndIsRead(String uuid, boolean b);
}
