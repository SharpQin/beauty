package cc.microthink.order.service;

import cc.microthink.common.dto.product.ProductDTO;
import cc.microthink.order.client.product.ProductClient;
import cc.microthink.order.repository.OrderRepository;
import cc.microthink.order.security.SecurityUtils;
import cc.microthink.order.service.dto.CreateOrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MKOrderService {

    private Logger log = LoggerFactory.getLogger(MKOrderService.class);

    private final OrderRepository orderRepository;

    private final ProductClient productClient;

    public MKOrderService(OrderRepository orderRepository, ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
    }

    public void createOrder(CreateOrderDTO orderDTO) {
        if (SecurityUtils.getCurrentUserLogin().isPresent()) {
            log.info("getProductById: loginUser:{}", SecurityUtils.getCurrentUserLogin().get());
        }
        ProductDTO productDTO = this.productClient.getProductById(orderDTO.getProductId());
        log.info("createOrder: productDTO.name:{}", productDTO.getName());
    }

}
