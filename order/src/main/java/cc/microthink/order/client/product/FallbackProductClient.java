package cc.microthink.order.client.product;

import cc.microthink.common.dto.product.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FallbackProductClient implements ProductClient {

    private Logger log = LoggerFactory.getLogger(FallbackProductClient.class);

    @Override
    public ProductDTO getProductById(Long id) {
        log.info("------FallbackProductClient.getProductById: id:{}", id);
        ProductDTO defaultDTO = new ProductDTO();
        defaultDTO.setId(id);
        defaultDTO.setName("EMPTY");
        return defaultDTO;
    }
}
