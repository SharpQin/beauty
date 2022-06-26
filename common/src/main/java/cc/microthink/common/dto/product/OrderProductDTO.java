package cc.microthink.common.dto.product;

public class OrderProductDTO extends ProductDTO{

    private Integer orderCount;

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public void updateStatusByOrderCount() {
        if (this.getStock() < this.getOrderCount()) {
            this.setStatus(ProductStatus.SELL_OUT);
        }
    }
}
