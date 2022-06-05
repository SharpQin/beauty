package cc.microthink.auth.repository;

import cc.microthink.auth.domain.Authority;
import cc.microthink.auth.repository.rowmapper.AuthorityRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Menu entity.
 */
@SuppressWarnings("unused")
class AuthorityRepositoryInternalImpl extends SimpleR2dbcRepository<Authority, Long> implements AuthorityRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final AuthorityRowMapper menuMapper;

    private static final Table entityTable = Table.aliased("au_authority", EntityManager.ENTITY_ALIAS);

    public AuthorityRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        AuthorityRowMapper menuMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Authority.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.menuMapper = menuMapper;
    }

    @Override
    public Flux<Authority> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Authority> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = MenuSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Authority.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Authority> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Authority> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Authority process(Row row, RowMetadata metadata) {
        Authority entity = menuMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Authority> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
