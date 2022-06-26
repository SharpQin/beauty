package cc.microthink.order.client.product;

import cc.microthink.common.dto.order.OrderItemDTO;
import cc.microthink.common.dto.product.OrderProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product", fallback = FallbackProductClient.class)
public interface ProductClient {

    @GetMapping( path ="/mki/product/{id}")
    OrderProductDTO getProductById(@PathVariable("id") Long id);

    @PostMapping( path ="/mki/productsByOrder")
    List<OrderProductDTO> getProductsByOrderItems(@RequestBody List<OrderItemDTO> items);

}
