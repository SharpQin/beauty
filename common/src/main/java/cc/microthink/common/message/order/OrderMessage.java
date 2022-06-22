package cc.microthink.common.message.order;

import java.util.ArrayList;
import java.util.List;

public class OrderMessage {

    public static final String ACTION_CREATE = "CRE";
    public static final String ACTION_CANCEL = "CANCEL";

    private String action;

    private Long orderId;

    private String serialNo;

    private List<OrderItem> items = new ArrayList<>(5);

    public OrderMessage() {}

    public OrderMessage(String actionType, Long orderId, String serialNo) {
        this.action = actionType;
        this.orderId = orderId;
        this.serialNo = serialNo;
        this.items = items;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void addItems(OrderItem item) {
        this.items.add(item);
    }

    public boolean isCreate() {
        return ACTION_CREATE.equals(this.action);
    }

    public boolean isCancel() {
        return ACTION_CANCEL.equals(this.action);
    }

    public static class OrderItem {

        private Long prodId;

        private Integer quantity;

        public OrderItem() {}

        public OrderItem(Long prodId, Integer quantity) {
            this.prodId = prodId;
            this.quantity = quantity;
        }

        public Long getProdId() {
            return prodId;
        }

        public void setProdId(Long prodId) {
            this.prodId = prodId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

}
