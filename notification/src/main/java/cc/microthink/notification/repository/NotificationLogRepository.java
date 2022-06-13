package cc.microthink.notification.repository;

import cc.microthink.notification.domain.NotificationLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the NotificationLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {}
