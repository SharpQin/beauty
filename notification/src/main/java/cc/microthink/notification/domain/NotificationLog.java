package cc.microthink.notification.domain;

import cc.microthink.notification.domain.enumeration.NotifyStatus;
import cc.microthink.notification.domain.enumeration.NotifyType;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A NotificationLog.
 */
@Entity
@Table(name = "notification_log")
public class NotificationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "cust_id")
    private Long custId;

    @Column(name = "cust_name")
    private String custName;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "notify_type")
    private NotifyType notifyType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NotifyStatus status;

    @Column(name = "notify_time")
    private Instant notifyTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NotificationLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustId() {
        return this.custId;
    }

    public NotificationLog custId(Long custId) {
        this.setCustId(custId);
        return this;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return this.custName;
    }

    public NotificationLog custName(String custName) {
        this.setCustName(custName);
        return this;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getTitle() {
        return this.title;
    }

    public NotificationLog title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public NotificationLog content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NotifyType getNotifyType() {
        return this.notifyType;
    }

    public NotificationLog notifyType(NotifyType notifyType) {
        this.setNotifyType(notifyType);
        return this;
    }

    public void setNotifyType(NotifyType notifyType) {
        this.notifyType = notifyType;
    }

    public NotifyStatus getStatus() {
        return this.status;
    }

    public NotificationLog status(NotifyStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(NotifyStatus status) {
        this.status = status;
    }

    public Instant getNotifyTime() {
        return this.notifyTime;
    }

    public NotificationLog notifyTime(Instant notifyTime) {
        this.setNotifyTime(notifyTime);
        return this;
    }

    public void setNotifyTime(Instant notifyTime) {
        this.notifyTime = notifyTime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationLog)) {
            return false;
        }
        return id != null && id.equals(((NotificationLog) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationLog{" +
            "id=" + getId() +
            ", custId=" + getCustId() +
            ", custName='" + getCustName() + "'" +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", notifyType='" + getNotifyType() + "'" +
            ", status='" + getStatus() + "'" +
            ", notifyTime='" + getNotifyTime() + "'" +
            "}";
    }
}
