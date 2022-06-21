package cc.microthink.product.service;

import cc.microthink.common.dto.product.ProductDTO;
import cc.microthink.product.domain.Product;
import cc.microthink.product.repository.ProductRepository;
import cc.microthink.product.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
