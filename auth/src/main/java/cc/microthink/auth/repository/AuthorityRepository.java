package cc.microthink.auth.repository;

import cc.microthink.auth.domain.Authority;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Menu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorityRepository extends ReactiveCrudRepository<Authority, Long>, AuthorityRepositoryInternal {
    Flux<Authority> findAllBy(Pageable pageable);

    @Override
    <S extends Authority> Mono<S> save(S entity);

    @Override
    Flux<Authority> findAll();

    @Override
    Mono<Authority> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AuthorityRepositoryInternal {
    <S extends Authority> Mono<S> save(S entity);

    Flux<Authority> findAllBy(Pageable pageable);

    Flux<Authority> findAll();

    Mono<Authority> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Menu> findAllBy(Pageable pageable, Criteria criteria);

}
