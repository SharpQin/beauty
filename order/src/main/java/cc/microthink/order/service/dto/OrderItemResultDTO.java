package cc.microthink.order.service.dto;

import cc.microthink.order.domain.OrderItem;

import java.math.BigDecimal;

public class OrderItemResultDTO {

    private Long id;

    private String name;

    private BigDecimal price;

    private String image;

    private Integer orderCount;

    public OrderItemResultDTO() {}

    public OrderItemResultDTO(Long id, String name, BigDecimal price, String image, Integer orderCount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.orderCount = orderCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }
}
