package cc.microthink.notification.message.in;

import cc.microthink.common.message.notify.*;
import cc.microthink.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class MessageConsumer {

    private Logger log = LoggerFactory.getLogger(MessageConsumer.class);

    private final NotificationService notificationService;

    public MessageConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Bean
    public Consumer<NotifyMessage> notificationConsumer() {
        return msg -> {
            log.info("notificationConsumer: msg:{}", msg);
            notificationService.notifyByType(msg);
        };
    }

}
