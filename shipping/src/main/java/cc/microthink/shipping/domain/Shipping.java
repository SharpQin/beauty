package cc.microthink.shipping.domain;

import cc.microthink.shipping.domain.enumeration.ShippingStatus;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A Shipping.
 */
@Entity
@Table(name = "shipping")
public class Shipping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "order_serial")
    private String orderSerial;

    @Column(name = "cust_id")
    private Long custId;

    @Column(name = "cust_address")
    private String custAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ShippingStatus status;

    @Column(name = "created_time")
    private Instant createdTime;

    @Column(name = "sent_time")
    private Instant sentTime;

    @Column(name = "achieved_time")
    private Instant achievedTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Shipping id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public Shipping orderId(Long orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderSerial() {
        return this.orderSerial;
    }

    public Shipping orderSerial(String orderSerial) {
        this.setOrderSerial(orderSerial);
        return this;
    }

    public void setOrderSerial(String orderSerial) {
        this.orderSerial = orderSerial;
    }

    public Long getCustId() {
        return this.custId;
    }

    public Shipping custId(Long custId) {
        this.setCustId(custId);
        return this;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getCustAddress() {
        return this.custAddress;
    }

    public Shipping custAddress(String custAddress) {
        this.setCustAddress(custAddress);
        return this;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public ShippingStatus getStatus() {
        return this.status;
    }

    public Shipping status(ShippingStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ShippingStatus status) {
        this.status = status;
    }

    public Instant getCreatedTime() {
        return this.createdTime;
    }

    public Shipping createdTime(Instant createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getSentTime() {
        return this.sentTime;
    }

    public Shipping sentTime(Instant sentTime) {
        this.setSentTime(sentTime);
        return this;
    }

    public void setSentTime(Instant sentTime) {
        this.sentTime = sentTime;
    }

    public Instant getAchievedTime() {
        return this.achievedTime;
    }

    public Shipping achievedTime(Instant achievedTime) {
        this.setAchievedTime(achievedTime);
        return this;
    }

    public void setAchievedTime(Instant achievedTime) {
        this.achievedTime = achievedTime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shipping)) {
            return false;
        }
        return id != null && id.equals(((Shipping) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Shipping{" +
            "id=" + getId() +
            ", orderId=" + getOrderId() +
            ", orderSerial='" + getOrderSerial() + "'" +
            ", custId=" + getCustId() +
            ", custAddress='" + getCustAddress() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", sentTime='" + getSentTime() + "'" +
            ", achievedTime='" + getAchievedTime() + "'" +
            "}";
    }
}
