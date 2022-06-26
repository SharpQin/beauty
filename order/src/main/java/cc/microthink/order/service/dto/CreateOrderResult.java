package cc.microthink.order.service.dto;

import java.util.List;

public class CreateOrderResult {

    private Long orderId;

    private String serialNo;

    private List<OrderItemResultDTO> items;

    private String status;

    public CreateOrderResult() {}

    public CreateOrderResult(Long orderId, String serialNo, String status) {
        this.orderId = orderId;
        this.serialNo = serialNo;
        this.status = status;
    }

    public CreateOrderResult(Long orderId, String serialNo, List<OrderItemResultDTO> items, String status) {
        this.orderId = orderId;
        this.serialNo = serialNo;
        this.items = items;
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

    public List<OrderItemResultDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResultDTO> items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
