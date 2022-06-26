package cc.microthink.order.client.product;

import cc.microthink.common.dto.order.OrderItemDTO;
import cc.microthink.common.dto.product.OrderProductDTO;
import cc.microthink.common.dto.product.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FallbackProductClient implements ProductClient {

    private Logger log = LoggerFactory.getLogger(FallbackProductClient.class);

    @Override
    public OrderProductDTO getProductById(Long id) {
        log.info("------FallbackProductClient.getProductById: id:{}", id);
        OrderProductDTO defaultDTO = new OrderProductDTO();
        defaultDTO.setId(id);
        defaultDTO.setName("EMPTY");
        return defaultDTO;
    }

    @Override
    public List<OrderProductDTO> getProductsByOrderItems(List<OrderItemDTO> items) {
        return null;
    }
}
