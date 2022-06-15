package cc.microthink.notification.service;

import cc.microthink.common.message.notify.NotifyMessage;
import org.springframework.stereotype.Service;

@Service("sms")
public class SMSNotify implements INotify{
    @Override
    public boolean notify(NotifyMessage notifyMessage) {
        return false;
    }

    @Override
    public String getType() {
        return NotifyMessage.NOTIFY_SMS;
    }
}
