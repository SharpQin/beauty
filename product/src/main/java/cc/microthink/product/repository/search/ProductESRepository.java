package cc.microthink.product.repository.search;

import cc.microthink.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductESRepository extends ElasticsearchRepository<Product, Long> {

    List<Product> findByName(String name);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"name\": \"?0\"}}]}}")
    Page<Product> findByCustomCondiction(String name, Pageable pageable);

}
