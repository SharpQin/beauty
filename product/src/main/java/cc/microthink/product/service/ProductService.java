package cc.microthink.product.service;

import cc.microthink.product.domain.Product;
import cc.microthink.product.repository.ProductRepository;
import cc.microthink.product.repository.search.ProductESRepository;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final ProductESRepository productESRepository;

    private final ElasticsearchOperations elasticsearchOperations;

    //private RestHighLevelClient elasticsearchClient;

    public ProductService(ProductRepository productRepository, ProductESRepository productESRepository, ElasticsearchOperations elasticsearchOperations) {
        this.productRepository = productRepository;
        this.productESRepository = productESRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    /**
     * Save a product.
     *
     * @param product the entity to save.
     * @return the persisted entity.
     */
    //@AclSave
    public Product save(Product product) {
        log.debug("Request to save Product : {}", product);
        Product savedProduct = productRepository.save(product);

        productESRepository.save(product);

        return savedProduct;
    }

    /**
     * Update a product.
     *
     * @param product the entity to save.
     * @return the persisted entity.
     */
    //@PreAuthorize("hasPermission(#product, write)")
    public Product update(Product product) {
        log.debug("Request to save Product : {}", product);
        Product savedProduct = productRepository.save(product);

        productESRepository.save(product);

        return savedProduct;
    }

    /**
     * Partially update a product.
     *
     * @param product the entity to update partially.
     * @return the persisted entity.
     */
    //@PreAuthorize("hasPermission(#product, write)")
    public Optional<Product> partialUpdate(Product product) {
        log.debug("Request to partially update Product : {}", product);
        return productRepository
            .findById(product.getId())
            .map(existingProduct -> {
                if (product.getName() != null) {
                    existingProduct.setName(product.getName());
                }
                if (product.getPrice() != null) {
                    existingProduct.setPrice(product.getPrice());
                }
                if (product.getType() != null) {
                    existingProduct.setType(product.getType());
                }
                if (product.getImage() != null) {
                    existingProduct.setImage(product.getImage());
                }
                if (product.getReleaseDate() != null) {
                    existingProduct.setReleaseDate(product.getReleaseDate());
                }
                if (product.getLiveTime() != null) {
                    existingProduct.setLiveTime(product.getLiveTime());
                }
                if (product.getCreatedTime() != null) {
                    existingProduct.setCreatedTime(product.getCreatedTime());
                }
                if (product.getUpdatedTime() != null) {
                    existingProduct.setUpdatedTime(product.getUpdatedTime());
                }
                if (product.getStock() != null) {
                    existingProduct.setStock(product.getStock());
                }
                if (product.getShowed() != null) {
                    existingProduct.setShowed(product.getShowed());
                }
                if (product.getStatus() != null) {
                    existingProduct.setStatus(product.getStatus());
                }
                if (product.getDsc() != null) {
                    existingProduct.setDsc(product.getDsc());
                }

                return existingProduct;
            })
            .map(productRepository::save);
    }

    /**
     * Get all the products.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAll(pageable);
    }

    /**
     * Get all the products with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Product> findAllWithEagerRelationships(Pageable pageable) {
        return productRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one product by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    //@PreAuthorize("hasPermission(#id, 'cc.microthink.product.domain.Product', admin)")
    public Optional<Product> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the product by id.
     *
     * @param id the id of the entity.
     */
    //@AclDelete(targetClass = Product.class)
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);

        productESRepository.deleteById(id);
    }

    //-- Search Code Sample

    /**
     * Index with elasticsearchOperations
     * @param product
     */
    public void indexProduct(Product product) {
        IndexQuery indexQuery = new IndexQueryBuilder()
            .withId(product.getId().toString())
            .withObject(product)
            .build();
        IndexCoordinates indexCoordinates = IndexCoordinates.of("product_index");
        String documentId = elasticsearchOperations.index(indexQuery, indexCoordinates);
        log.info("indexProduct: documentId:{}", documentId);

        //elasticsearchOperations.bulkIndex()
    }

    /**
     * Get object by elasticsearchOperations
     * @param id
     */
    public void findByProductId(Long id) {
        Product product = elasticsearchOperations.get(id.toString(), Product.class);
        log.info("findByProductId: product:{}", product);
    }

    /**
     * Find objects by repository method
     * @param name
     * @param pageable
     * @return
     */
    public Page<Product> findProducts(String name, Pageable pageable) {
        Page<Product> result = productESRepository.findByName(name, pageable);
        log.info("findProducts: result:{}", result);
        return result;
    }

    /**
     * NativeQuery
     * NativeQuery provides the maximum flexibility for building a query using objects representing Elasticsearch constructs like aggregation, filter, and sort.
     * @param name
     */
    public void searchProductByNativeQuery(String name) {
        //QueryBuilder queryBuilder = QueryBuilders.matchQuery("name", name); //matched exactly
        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(name, "name", "dsc").fuzziness(Fuzziness.AUTO);
        //Query searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
        Query searchQuery = new NativeSearchQueryBuilder().withFilter(queryBuilder).build();

        SearchHits<Product> productHits = elasticsearchOperations.search(searchQuery, Product.class, IndexCoordinates.of("product_index"));
        log.info("searchProductByNativeQuery: productHits:{}", productHits);
    }

    public void searchProductByNativeQuery2(String name) {
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("name", name + "*");
        Query searchQuery = new NativeSearchQueryBuilder().withFilter(queryBuilder).withPageable(PageRequest.of(0, 5)).build();
        SearchHits<Product> productHits = elasticsearchOperations.search(searchQuery, Product.class, IndexCoordinates.of("product_index"));
        log.info("searchProductByNativeQuery2: productHits:{}", productHits);
    }

    /**
     * StringQuery
     * A StringQuery gives full control by allowing the use of the native Elasticsearch query as a JSON string.
     * @param name
     */
    public void searchProductByStringQuery(String name) {

        Query searchQuery = new StringQuery(
            "{\"match\":{\"name\":{\"query\":\""+ name + "\"}}}\"");

        SearchHits<Product> productHits = elasticsearchOperations.search(searchQuery, Product.class, IndexCoordinates.of("product_index"));
        log.info("searchProductByStringQuery: productHits:{}", productHits);
    }

    /**
     * CriteriaQuery
     * With CriteriaQuery we can build queries without knowing any terminology of Elasticsearch.
     * @param name
     */
    public void searchProductByCriteriaQuery(String name) {

        Criteria criteria = new Criteria("name").contains(name);
            //.greaterThan(10.0)
            //.lessThan(100.0);

        Query searchQuery = new CriteriaQuery(criteria);

        SearchHits<Product> productHits = elasticsearchOperations.search(searchQuery, Product.class, IndexCoordinates.of("product_index"));
        log.info("searchProductByCriteriaQuery: productHits:{}", productHits);
    }
}
