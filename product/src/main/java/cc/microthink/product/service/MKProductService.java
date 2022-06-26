package cc.microthink.product.service;

import cc.microthink.common.dto.order.OrderItemDTO;
import cc.microthink.common.dto.product.OrderProductDTO;
import cc.microthink.common.dto.product.ProductDTO;
import cc.microthink.common.message.order.OrderMessage;
import cc.microthink.product.domain.Product;
import cc.microthink.product.repository.ProductRepository;
import cc.microthink.product.security.SecurityUtils;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MKProductService {

    private Logger log = LoggerFactory.getLogger(MKProductService.class);

    private final ProductRepository productRepository;

    public MKProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private OrderProductDTO changeToDTO(Product product) {
        OrderProductDTO dto = new OrderProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setImage(product.getImage());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setType(ProductDTO.ProductType.valueOf(product.getType().toString()));
        dto.setStatus(ProductDTO.ProductStatus.valueOf(product.getStatus().toString()));
        return dto;
    }

    @Transactional(readOnly = true)
    public OrderProductDTO getProductById(Long id, Integer orderCount) {

        if (SecurityUtils.getCurrentUserLogin().isPresent()) {
            log.info("getProductById: loginUser:{}", SecurityUtils.getCurrentUserLogin().get());
        }

        OrderProductDTO dto;
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            dto = changeToDTO(product);
            dto.setOrderCount(orderCount);
            dto.updateStatusByOrderCount();
        }
        else {
            throw new RuntimeException("No product with id:" + id);
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public List<OrderProductDTO> getProductsByOrderItems(@RequestBody List<OrderItemDTO> items) {
        List<Long> ids = items.stream().map(item -> item.getProductId()).collect(Collectors.toList());
        Map<Long, Integer> productIdCountMap = Maps.newHashMap();
        items.forEach(item -> productIdCountMap.put(item.getProductId(), item.getCount()));
        List<Product> products = productRepository.findAllById(ids);
        return products.stream().map(product -> {
            OrderProductDTO orderProductDTO = this.changeToDTO(product);
            Integer orderCount = productIdCountMap.get(orderProductDTO.getId());
            orderProductDTO.setOrderCount(orderCount);
            orderProductDTO.updateStatusByOrderCount();
            return orderProductDTO;
        }).collect(Collectors.toList());
    }

    public boolean consumeProduct(String msgId, OrderMessage orderMsg) {
        log.debug("consumeProduct:msgId:{}, orderMsg:{}", msgId, orderMsg);
        boolean isOk = true;
        //check if exist msgId

        //update product stock: decrease stock
        List<Product> expectList = new ArrayList<>(orderMsg.getItems().size());
        for (OrderMessage.OrderItem item : orderMsg.getItems()) {
            Long prodId = item.getProdId();
            Integer quality = item.getQuantity();
            Product product = productRepository.getById(prodId);
            if (product.getStock() < item.getQuantity()) {
                isOk = false;
                expectList.clear();
                break;
            }
            product.decrease(item.getQuantity());
            expectList.add(product);
        }

        if (isOk) {
            productRepository.saveAll(expectList);
        }

        return isOk;
    }

    public void recoverProduct(String msgId, OrderMessage orderMsg) {
        log.debug("recoverProduct:msgId:{}, orderMsg:{}", msgId, orderMsg);
        //check if exist msgId

        //update product stock: increase stock
        List<Product> expectList = new ArrayList<>(orderMsg.getItems().size());
        for (OrderMessage.OrderItem item : orderMsg.getItems()) {
            Long prodId = item.getProdId();
            Integer quality = item.getQuantity();
            Product product = productRepository.getById(prodId);
            product.increaseStock(item.getQuantity());
            expectList.add(product);
        }
        productRepository.saveAll(expectList);
    }

}
