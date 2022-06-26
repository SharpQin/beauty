package cc.microthink.product.web.rest.market;

import cc.microthink.common.dto.order.OrderItemDTO;
import cc.microthink.common.dto.product.OrderProductDTO;
import cc.microthink.product.service.MKProductService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mki")
public class MKProductResource {

    private final MKProductService productService;

    public MKProductResource(MKProductService productService) {
        this.productService = productService;
    }

    /**
     * ResponseEntity<ProductDTO> or ProductDTO
     * @param id
     * @return
     */
    @GetMapping("/product/{id}")
    ResponseEntity<OrderProductDTO> getProductById(@PathVariable("id") Long id, @Param("orderCount") Integer orderCount) {
        return ResponseEntity.ok().body(productService.getProductById(id, orderCount));
    }

    @PostMapping( path ="/productsByOrder")
    List<OrderProductDTO> getProductsByOrderItems(@RequestBody List<OrderItemDTO> items) {
        return productService.getProductsByOrderItems(items);
    }

}
