package cc.microthink.notification.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationFactory {

    private final INotify emailNotify;

    private final INotify smsNotify;

    private final Map<String, INotify> notifyMap = new HashMap<>(5);

    public NotificationFactory(@Qualifier("email")INotify emailNotify, @Qualifier("sms")INotify smsNotify) {
        this.emailNotify = emailNotify;
        this.smsNotify = smsNotify;

        this.notifyMap.put(emailNotify.getType(), emailNotify);
        this.notifyMap.put(smsNotify.getType(), smsNotify);
    }

    public INotify getNotification(String type) {
        return this.notifyMap.get(type);
    }

}
