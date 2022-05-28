package cc.microthink.product.web.rest;

import cc.microthink.product.domain.ProductItem;
import cc.microthink.product.repository.ProductItemRepository;
import cc.microthink.product.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link cc.microthink.product.domain.ProductItem}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProductItemResource {

    private final Logger log = LoggerFactory.getLogger(ProductItemResource.class);

    private static final String ENTITY_NAME = "productProductItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductItemRepository productItemRepository;

    public ProductItemResource(ProductItemRepository productItemRepository) {
        this.productItemRepository = productItemRepository;
    }

    /**
     * {@code POST  /product-items} : Create a new productItem.
     *
     * @param productItem the productItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productItem, or with status {@code 400 (Bad Request)} if the productItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-items")
    public ResponseEntity<ProductItem> createProductItem(@RequestBody ProductItem productItem) throws URISyntaxException {
        log.debug("REST request to save ProductItem : {}", productItem);
        if (productItem.getId() != null) {
            throw new BadRequestAlertException("A new productItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductItem result = productItemRepository.save(productItem);
        return ResponseEntity
            .created(new URI("/api/product-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-items/:id} : Updates an existing productItem.
     *
     * @param id the id of the productItem to save.
     * @param productItem the productItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productItem,
     * or with status {@code 400 (Bad Request)} if the productItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-items/{id}")
    public ResponseEntity<ProductItem> updateProductItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductItem productItem
    ) throws URISyntaxException {
        log.debug("REST request to update ProductItem : {}, {}", id, productItem);
        if (productItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductItem result = productItemRepository.save(productItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-items/:id} : Partial updates given fields of an existing productItem, field will ignore if it is null
     *
     * @param id the id of the productItem to save.
     * @param productItem the productItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productItem,
     * or with status {@code 400 (Bad Request)} if the productItem is not valid,
     * or with status {@code 404 (Not Found)} if the productItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the productItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductItem> partialUpdateProductItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductItem productItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductItem partially : {}, {}", id, productItem);
        if (productItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductItem> result = productItemRepository
            .findById(productItem.getId())
            .map(existingProductItem -> {
                if (productItem.getName() != null) {
                    existingProductItem.setName(productItem.getName());
                }
                if (productItem.getAmount() != null) {
                    existingProductItem.setAmount(productItem.getAmount());
                }
                if (productItem.getImage() != null) {
                    existingProductItem.setImage(productItem.getImage());
                }
                if (productItem.getDsc() != null) {
                    existingProductItem.setDsc(productItem.getDsc());
                }

                return existingProductItem;
            })
            .map(productItemRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productItem.getId().toString())
        );
    }

    /**
     * {@code GET  /product-items} : get all the productItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productItems in body.
     */
    @GetMapping("/product-items")
    public List<ProductItem> getAllProductItems() {
        log.debug("REST request to get all ProductItems");
        return productItemRepository.findAll();
    }

    /**
     * {@code GET  /product-items/:id} : get the "id" productItem.
     *
     * @param id the id of the productItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-items/{id}")
    public ResponseEntity<ProductItem> getProductItem(@PathVariable Long id) {
        log.debug("REST request to get ProductItem : {}", id);
        Optional<ProductItem> productItem = productItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productItem);
    }

    /**
     * {@code DELETE  /product-items/:id} : delete the "id" productItem.
     *
     * @param id the id of the productItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-items/{id}")
    public ResponseEntity<Void> deleteProductItem(@PathVariable Long id) {
        log.debug("REST request to delete ProductItem : {}", id);
        productItemRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
