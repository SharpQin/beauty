package cc.microthink.notification.service;

import cc.microthink.common.message.notify.NotifyMessage;
import cc.microthink.notification.domain.NotificationLog;
import cc.microthink.notification.domain.enumeration.NotifyStatus;
import cc.microthink.notification.domain.enumeration.NotifyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class NotificationService {

    private Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationLogService logService;

    private final NotificationFactory notificationFactory;

    public NotificationService(NotificationLogService logService, NotificationFactory notificationFactory) {
        this.logService = logService;
        this.notificationFactory = notificationFactory;
    }

    public void notifyByType(NotifyMessage notifyMessage) {
        log.info("notifyByType: notifyMessage:{}", notifyMessage);

        INotify notification = notificationFactory.getNotification(notifyMessage.getNotifyType());
        boolean success = notification.notify(notifyMessage);
        if (success) {
            log.info("notifyByType: Success to send notification. notifyMessage:{}", notifyMessage);
        }
        else {
            log.warn("notifyByType: Fail to send notification. notifyMessage:{}", notifyMessage);
        }

        NotificationLog notificationLog = new NotificationLog();
        notificationLog.setCustId(notifyMessage.getPrincipal().getCustId());
        notificationLog.setCustName(notifyMessage.getPrincipal().getCustName());
        notificationLog.setNotifyType(NotifyType.valueOf(notifyMessage.getNotifyType()));
        notificationLog.setContent(notifyMessage.getContent().getContent());
        notificationLog.setTitle(notifyMessage.getContent().getSubject());
        notificationLog.setStatus(NotifyStatus.valueOf(success ? "SUCCESS" : "FAIL"));
        notificationLog.setNotifyTime(Instant.now());
        logService.save(notificationLog);
    }
}
