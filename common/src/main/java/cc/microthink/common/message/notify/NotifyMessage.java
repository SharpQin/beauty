package cc.microthink.common.message.notify;

import java.io.Serializable;

public class NotifyMessage implements Serializable {

    public static final String NOTIFY_EMAIL = "EMAIL";
    public static final String NOTIFY_SMS = "SMS";
    public static final String NOTIFY_WECHAT = "WECHAT";

    private String notifyType;

    private MessagePrincipal principal;

    private MessageContent content;

    public NotifyMessage() {}

    public NotifyMessage(String notifyType) {
        this.notifyType = notifyType;
    }

    public NotifyMessage(String notifyType, MessagePrincipal principal, MessageContent content) {
        this.notifyType = notifyType;
        this.principal = principal;
        this.content = content;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public MessagePrincipal getPrincipal() {
        return principal;
    }

    public void setPrincipal(MessagePrincipal principal) {
        this.principal = principal;
    }

    public MessageContent getContent() {
        return content;
    }

    public void setContent(MessageContent content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "NotifyMessage{" +
                "notifyType='" + notifyType + '\'' +
                ", principal=" + principal +
                ", content=" + content +
                '}';
    }
}
