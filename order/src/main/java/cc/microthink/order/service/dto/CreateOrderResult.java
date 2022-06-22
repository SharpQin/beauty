package cc.microthink.order.service.dto;

public class CreateOrderResult {

    private Long orderId;

    private String serialNo;

    private String product;

    private String status;

    public CreateOrderResult() {}

    public CreateOrderResult(Long orderId, String serialNo, String product, String status) {
        this.orderId = orderId;
        this.serialNo = serialNo;
        this.product = product;
        this.status = status;
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

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
