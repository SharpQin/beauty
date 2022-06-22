package cc.microthink.common.message.order;

public class OrderResMessage {

    private String action;

    private Long orderId;

    private String serialNo;

    private boolean success;

    public OrderResMessage() {}

    public OrderResMessage(String action, Long orderId, String serialNo) {
        this(action, orderId, serialNo, false);
    }

    public OrderResMessage(String action, Long orderId, String serialNo, boolean success) {
        this.action = action;
        this.orderId = orderId;
        this.serialNo = serialNo;
        this.success = success;
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isCreate() {
        return OrderMessage.ACTION_CREATE.equals(this.action);
    }

    public boolean isCancel() {
        return OrderMessage.ACTION_CANCEL.equals(this.action);
    }
}
