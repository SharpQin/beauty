package cc.microthink.auth.repository;

import cc.microthink.auth.domain.Menu;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Menu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuRepository extends ReactiveCrudRepository<Menu, Long>, MenuRepositoryInternal {
    Flux<Menu> findAllBy(Pageable pageable);

    @Override
    <S extends Menu> Mono<S> save(S entity);

    @Override
    Flux<Menu> findAll();

    @Override
    Mono<Menu> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface MenuRepositoryInternal {
    <S extends Menu> Mono<S> save(S entity);

    Flux<Menu> findAllBy(Pageable pageable);

    Flux<Menu> findAll();

    Mono<Menu> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Menu> findAllBy(Pageable pageable, Criteria criteria);

}
