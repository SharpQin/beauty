package cc.microthink.auth.repository;

import cc.microthink.auth.domain.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Role entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoleRepository extends ReactiveCrudRepository<Role, Long>, RoleRepositoryInternal {
    Flux<Role> findAllBy(Pageable pageable);

    @Override
    Mono<Role> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Role> findAllWithEagerRelationships();

    @Override
    Flux<Role> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM role entity JOIN rel_role__permissions joinTable ON entity.id = joinTable.permissions_id WHERE joinTable.permissions_id = :id"
    )
    Flux<Role> findByPermissions(Long id);

    @Override
    <S extends Role> Mono<S> save(S entity);

    @Override
    Flux<Role> findAll();

    @Override
    Mono<Role> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface RoleRepositoryInternal {
    <S extends Role> Mono<S> save(S entity);

    Flux<Role> findAllBy(Pageable pageable);

    Flux<Role> findAll();

    Mono<Role> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Role> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Role> findOneWithEagerRelationships(Long id);

    Flux<Role> findAllWithEagerRelationships();

    Flux<Role> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
