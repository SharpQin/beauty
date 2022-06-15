package cc.microthink.notification.service;

import cc.microthink.common.message.notify.NotifyMessage;

/**
 * Notify interface
 */
public interface INotify {

    public boolean notify(NotifyMessage notifyMessage);

    public String getType();

}
