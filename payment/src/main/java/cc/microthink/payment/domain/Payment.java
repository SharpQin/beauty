package cc.microthink.payment.domain;

import cc.microthink.payment.domain.enumeration.PaymentStatus;
import cc.microthink.payment.domain.enumeration.PaymentType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
public class Payment implements Serializable {

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

    @Column(name = "accinfo")
    private String accinfo;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "money", precision = 21, scale = 2, nullable = false)
    private BigDecimal money;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "payment_time")
    private Instant paymentTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public Payment orderId(Long orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderSerial() {
        return this.orderSerial;
    }

    public Payment orderSerial(String orderSerial) {
        this.setOrderSerial(orderSerial);
        return this;
    }

    public void setOrderSerial(String orderSerial) {
        this.orderSerial = orderSerial;
    }

    public Long getCustId() {
        return this.custId;
    }

    public Payment custId(Long custId) {
        this.setCustId(custId);
        return this;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getAccinfo() {
        return this.accinfo;
    }

    public Payment accinfo(String accinfo) {
        this.setAccinfo(accinfo);
        return this;
    }

    public void setAccinfo(String accinfo) {
        this.accinfo = accinfo;
    }

    public BigDecimal getMoney() {
        return this.money;
    }

    public Payment money(BigDecimal money) {
        this.setMoney(money);
        return this;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public PaymentType getPaymentType() {
        return this.paymentType;
    }

    public Payment paymentType(PaymentType paymentType) {
        this.setPaymentType(paymentType);
        return this;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    public Payment paymentStatus(PaymentStatus paymentStatus) {
        this.setPaymentStatus(paymentStatus);
        return this;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Instant getPaymentTime() {
        return this.paymentTime;
    }

    public Payment paymentTime(Instant paymentTime) {
        this.setPaymentTime(paymentTime);
        return this;
    }

    public void setPaymentTime(Instant paymentTime) {
        this.paymentTime = paymentTime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", orderId=" + getOrderId() +
            ", orderSerial='" + getOrderSerial() + "'" +
            ", custId=" + getCustId() +
            ", accinfo='" + getAccinfo() + "'" +
            ", money=" + getMoney() +
            ", paymentType='" + getPaymentType() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", paymentTime='" + getPaymentTime() + "'" +
            "}";
    }
}
