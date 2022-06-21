package cc.microthink.order.client.product;

import cc.microthink.common.dto.product.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product", fallback = FallbackProductClient.class)
public interface ProductClient {

    @GetMapping( path ="/mki/product/{id}")
    ProductDTO getProductById(@PathVariable("id") Long id);

}
