package cc.microthink.order.domain;

import cc.microthink.order.domain.enumeration.OrderCancelReason;
import cc.microthink.order.domain.enumeration.OrderItemStatus;
import cc.microthink.order.domain.enumeration.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Order.
 */
@Entity
@Table(name = "or_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "serial_no")
    private UUID serialNo;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "cust_id")
    private Long custId;

    @Column(name = "created_time")
    private Instant createdTime;

    @Column(name = "action_time")
    private Instant actionTime;

    @Column(name = "remark")
    private String remark;

    @Enumerated(EnumType.STRING)
    @Column(name = "cancel_reason")
    private OrderCancelReason cancelReason;

    //TODO add version field

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order() {}

    public Order(UUID serialNo, BigDecimal price, Long custId, String remark) {
        this.serialNo = serialNo;
        this.price = price;
        this.custId = custId;
        this.remark = remark;

        this.cancelReason = OrderCancelReason.NON;
        this.status = OrderStatus.PENDING;
        this.createdTime = Instant.now();
        this.actionTime = Instant.now();
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getSerialNo() {
        return this.serialNo;
    }

    public Order serialNo(UUID serialNo) {
        this.setSerialNo(serialNo);
        return this;
    }

    public void setSerialNo(UUID serialNo) {
        this.serialNo = serialNo;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Order price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public Order status(OrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getCustId() {
        return this.custId;
    }

    public Order custId(Long custId) {
        this.setCustId(custId);
        return this;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public Instant getCreatedTime() {
        return this.createdTime;
    }

    public Order createdTime(Instant createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getActionTime() {
        return this.actionTime;
    }

    public Order actionTime(Instant actionTime) {
        this.setActionTime(actionTime);
        return this;
    }

    public void setActionTime(Instant actionTime) {
        this.actionTime = actionTime;
    }

    public String getRemark() {
        return this.remark;
    }

    public Order remark(String remark) {
        this.setRemark(remark);
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<OrderItem> getItems() {
        return this.items;
    }

    public void setItems(List<OrderItem> orderItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setOrder(null));
        }
        if (orderItems != null) {
            orderItems.forEach(i -> i.setOrder(this));
        }
        this.items = orderItems;
    }

    public Order items(List<OrderItem> orderItems) {
        this.setItems(orderItems);
        return this;
    }

    public Order addItems(OrderItem orderItem) {
        this.items.add(orderItem);
        orderItem.setOrder(this);
        return this;
    }

    public Order removeItems(OrderItem orderItem) {
        this.items.remove(orderItem);
        orderItem.setOrder(null);
        return this;
    }

    public OrderCancelReason getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(OrderCancelReason cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Order cancelReason(OrderCancelReason cancelReason) {
        this.setCancelReason(cancelReason);
        return this;
    }

    public void cancelItems(OrderItemStatus itemStatus) {
        this.getItems().forEach(item -> item.setStatus(itemStatus));
    }

    public boolean isPending() {
        return (this.status == OrderStatus.PENDING);
    }

    public boolean isCreated() {
        return (this.status == OrderStatus.CREATED);
    }

    public boolean isCompleted() {
        return (this.status == OrderStatus.COMPLETED);
    }

    public boolean isCancelled() {
        return (this.status == OrderStatus.CANCELLED);
    }



    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", serialNo='" + getSerialNo() + "'" +
            ", price=" + getPrice() +
            ", status='" + getStatus() + "'" +
            ", custId=" + getCustId() +
            ", createdTime='" + getCreatedTime() + "'" +
            ", actionTime='" + getActionTime() + "'" +
            ", remark='" + getRemark() + "'" +
            "}";
    }
}
