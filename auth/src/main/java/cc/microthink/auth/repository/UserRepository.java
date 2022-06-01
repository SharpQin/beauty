package cc.microthink.auth.repository;

import cc.microthink.auth.domain.Authority;
import cc.microthink.auth.domain.Role;
import cc.microthink.auth.domain.User;
import org.apache.commons.beanutils.BeanComparator;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

/**
 * Spring Data R2DBC repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends R2dbcRepository<User, Long>, UserRepositoryInternal {
    Mono<User> findOneByActivationKey(String activationKey);

    Flux<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(LocalDateTime dateTime);

    Mono<User> findOneByResetKey(String resetKey);

    Mono<User> findOneByEmailIgnoreCase(String email);

    Mono<User> findOneByLogin(String login);

    Flux<User> findAllByIdNotNull(Pageable pageable);

    Flux<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

    Mono<Long> count();

    @Query("INSERT INTO au_user_role VALUES(:userId, :authority)")
    Mono<Void> saveUserAuthority(Long userId, Long authority);

    @Query("DELETE FROM au_user_role")
    Mono<Void> deleteAllUserAuthorities();

    @Query("DELETE FROM au_user_role WHERE user_id = :userId")
    Mono<Void> deleteUserAuthorities(Long userId);
}

interface DeleteExtended<T> {
    Mono<Void> delete(T user);
}

interface UserRepositoryInternal extends DeleteExtended<User> {
    Mono<User> findOneWithAuthoritiesByLogin(String login);

    Mono<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Flux<User> findAllWithAuthorities(Pageable pageable);
}

class UserRepositoryInternalImpl implements UserRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final R2dbcConverter r2dbcConverter;

    public UserRepositoryInternalImpl(DatabaseClient db, R2dbcEntityTemplate r2dbcEntityTemplate, R2dbcConverter r2dbcConverter) {
        this.db = db;
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
        this.r2dbcConverter = r2dbcConverter;
    }

    @Override
    public Mono<User> findOneWithAuthoritiesByLogin(String login) {
        return findOneWithAuthoritiesBy("login", login);
    }

    @Override
    public Mono<User> findOneWithAuthoritiesByEmailIgnoreCase(String email) {
        return findOneWithAuthoritiesBy("email", email.toLowerCase());
    }

    @Override
    public Flux<User> findAllWithAuthorities(Pageable pageable) {
        String property = pageable.getSort().stream().map(Sort.Order::getProperty).findFirst().orElse("id");
        String direction = String.valueOf(
            pageable.getSort().stream().map(Sort.Order::getDirection).findFirst().orElse(Sort.DEFAULT_DIRECTION)
        );
        long page = pageable.getPageNumber();
        long size = pageable.getPageSize();

        return db
            .sql("SELECT u.*, r.id AS role_id, r.name AS role_name FROM au_user u LEFT JOIN au_user_role ua ON u.id=ua.user_id LEFT JOIN role r ON ua.role_id = r.id")
            .map((row, metadata) ->
                Tuples.of(r2dbcConverter.read(User.class, row, metadata), Optional.ofNullable(row.get("role_id", Long.class)), Optional.ofNullable(row.get("role_name", String.class)))
            )
            .all()
            .groupBy(t -> t.getT1().getLogin())
            .flatMap(l -> l.collectList().map(t -> updateUserWithAuthorities(t.get(0).getT1(), t)))
            .sort(
                Sort.Direction.fromString(direction) == Sort.DEFAULT_DIRECTION
                    ? new BeanComparator<>(property)
                    : new BeanComparator<>(property).reversed()
            )
            .skip(page * size)
            .take(size);
    }

    @Override
    public Mono<Void> delete(User user) {
        return db
            .sql("DELETE FROM au_user_role WHERE user_id = :userId")
            .bind("userId", user.getId())
            .then()
            .then(r2dbcEntityTemplate.delete(User.class).matching(query(where("id").is(user.getId()))).all().then());
    }

//    private Mono<User> findOneWithAuthoritiesBy(String fieldName, Object fieldValue) {
//        return db
//            .sql("SELECT * FROM au_user u LEFT JOIN au_user_authority ua ON u.id=ua.user_id WHERE u." + fieldName + " = :" + fieldName)
//            .bind(fieldName, fieldValue)
//            .map((row, metadata) ->
//                Tuples.of(r2dbcConverter.read(User.class, row, metadata), Optional.ofNullable(row.get("authority_name", String.class)))
//            )
//            .all()
//            .collectList()
//            .filter(l -> !l.isEmpty())
//            .map(l -> updateUserWithAuthorities(l.get(0).getT1(), l));
//    }

    private Mono<User> findOneWithAuthoritiesBy(String fieldName, Object fieldValue) {
        String sql = String.format("SELECT u.*, r.id AS role_id, r.name AS role_name FROM au_user u LEFT JOIN au_user_role ua ON u.id=ua.user_id LEFT JOIN role r ON ua.role_id = r.id WHERE u.%s = :%s", fieldName, fieldName);
        return db.sql(sql)
            .bind(fieldName, fieldValue)
            .map((row, metadata) ->
                Tuples.of(r2dbcConverter.read(User.class, row, metadata), Optional.ofNullable(row.get("role_id", Long.class)), Optional.ofNullable(row.get("role_name", String.class)))
            )
            .all()
            .collectList()
            .filter(l -> !l.isEmpty())
            .map(l -> updateUserWithAuthorities(l.get(0).getT1(), l));
    }

//    private User updateUserWithAuthorities(User user, List<Tuple2<User, Optional<String>>> tuples) {
//        user.setAuthorities(
//            tuples
//                .stream()
//                .filter(t -> t.getT2().isPresent())
//                .map(t -> {
//                    Authority authority = new Authority();
//                    authority.setName(t.getT2().get());
//                    return authority;
//                })
//                .collect(Collectors.toSet())
//        );
//
//        return user;
//    }

    private User updateUserWithAuthorities(User user, List<Tuple3<User, Optional<Long>, Optional<String>>> tuples) {
        user.setRoles(
            tuples
                .stream()
                .filter(t -> t.getT2().isPresent())
                .map(t -> {
                    Role role = new Role();
                    role.setId(t.getT2().get());
                    role.setName(t.getT3().get());
                    return role;
                })
                .collect(Collectors.toSet())
        );

        return user;
    }
}
