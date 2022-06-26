package cc.microthink.order.service.dto;

import cc.microthink.common.dto.order.OrderItemDTO;

import java.util.List;

public class CreateOrderDTO {

    private List<OrderItemDTO> items;

    private Long addressId;

    private String remark;

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
