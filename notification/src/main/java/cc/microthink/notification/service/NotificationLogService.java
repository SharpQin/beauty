package cc.microthink.notification.service;

import cc.microthink.notification.domain.NotificationLog;
import cc.microthink.notification.repository.NotificationLogRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NotificationLog}.
 */
@Service
@Transactional
public class NotificationLogService {

    private final Logger log = LoggerFactory.getLogger(NotificationLogService.class);

    private final NotificationLogRepository notificationLogRepository;

    public NotificationLogService(NotificationLogRepository notificationLogRepository) {
        this.notificationLogRepository = notificationLogRepository;
    }

    /**
     * Save a notificationLog.
     *
     * @param notificationLog the entity to save.
     * @return the persisted entity.
     */
    public NotificationLog save(NotificationLog notificationLog) {
        log.debug("Request to save NotificationLog : {}", notificationLog);
        return notificationLogRepository.save(notificationLog);
    }

    /**
     * Update a notificationLog.
     *
     * @param notificationLog the entity to save.
     * @return the persisted entity.
     */
    public NotificationLog update(NotificationLog notificationLog) {
        log.debug("Request to save NotificationLog : {}", notificationLog);
        return notificationLogRepository.save(notificationLog);
    }

    /**
     * Partially update a notificationLog.
     *
     * @param notificationLog the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NotificationLog> partialUpdate(NotificationLog notificationLog) {
        log.debug("Request to partially update NotificationLog : {}", notificationLog);

        return notificationLogRepository
            .findById(notificationLog.getId())
            .map(existingNotificationLog -> {
                if (notificationLog.getCustId() != null) {
                    existingNotificationLog.setCustId(notificationLog.getCustId());
                }
                if (notificationLog.getCustName() != null) {
                    existingNotificationLog.setCustName(notificationLog.getCustName());
                }
                if (notificationLog.getTitle() != null) {
                    existingNotificationLog.setTitle(notificationLog.getTitle());
                }
                if (notificationLog.getContent() != null) {
                    existingNotificationLog.setContent(notificationLog.getContent());
                }
                if (notificationLog.getNotifyType() != null) {
                    existingNotificationLog.setNotifyType(notificationLog.getNotifyType());
                }
                if (notificationLog.getStatus() != null) {
                    existingNotificationLog.setStatus(notificationLog.getStatus());
                }
                if (notificationLog.getNotifyTime() != null) {
                    existingNotificationLog.setNotifyTime(notificationLog.getNotifyTime());
                }

                return existingNotificationLog;
            })
            .map(notificationLogRepository::save);
    }

    /**
     * Get all the notificationLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NotificationLog> findAll(Pageable pageable) {
        log.debug("Request to get all NotificationLogs");
        return notificationLogRepository.findAll(pageable);
    }

    /**
     * Get one notificationLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotificationLog> findOne(Long id) {
        log.debug("Request to get NotificationLog : {}", id);
        return notificationLogRepository.findById(id);
    }

    /**
     * Delete the notificationLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete NotificationLog : {}", id);
        notificationLogRepository.deleteById(id);
    }
}
