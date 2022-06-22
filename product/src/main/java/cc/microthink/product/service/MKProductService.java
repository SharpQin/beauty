package cc.microthink.product.service;

import cc.microthink.common.dto.product.ProductDTO;
import cc.microthink.common.message.order.OrderMessage;
import cc.microthink.product.domain.Product;
import cc.microthink.product.repository.ProductRepository;
import cc.microthink.product.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MKProductService {

    private Logger log = LoggerFactory.getLogger(MKProductService.class);

    private final ProductRepository productRepository;

    public MKProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {

        if (SecurityUtils.getCurrentUserLogin().isPresent()) {
            log.info("getProductById: loginUser:{}", SecurityUtils.getCurrentUserLogin().get());
        }

        ProductDTO dto = new ProductDTO();
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();

            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setImage(product.getImage());
            dto.setPrice(product.getPrice());
            dto.setStock(product.getStock());
            dto.setType(ProductDTO.ProductType.valueOf(product.getType().toString()));
            dto.setStatus(ProductDTO.ProductStatus.valueOf(product.getStatus().toString()));
        }
        else {
            throw new RuntimeException("No product with id:" + id);
        }

        return dto;
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
