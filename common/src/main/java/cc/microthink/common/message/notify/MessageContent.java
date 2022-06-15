package cc.microthink.common.message.notify;

import java.io.Serializable;

public class MessageContent implements Serializable {

    private String subject;

    private String content;

    public MessageContent() {}

    public MessageContent(String content) {
        this.content = content;
    }

    public MessageContent(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MessageContent{" +
                "subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
