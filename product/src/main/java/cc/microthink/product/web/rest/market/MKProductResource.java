package cc.microthink.product.web.rest.market;

import cc.microthink.common.dto.product.ProductDTO;
import cc.microthink.product.service.MKProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    ResponseEntity<ProductDTO> getProductById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(productService.getProductById(id));
    }

}
