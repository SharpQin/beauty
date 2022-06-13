package cc.microthink.auth.repository;

import cc.microthink.auth.domain.MKUser;
import cc.microthink.auth.domain.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public interface MKUserRepository extends ReactiveCrudRepository<MKUser, Long> {

    Mono<MKUser> findOneByActivationKey(String activationKey);

    Flux<MKUser> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(LocalDateTime dateTime);

    Mono<MKUser> findOneByResetKey(String resetKey);

    Mono<MKUser> findOneByEmailIgnoreCase(String email);

    Mono<MKUser> findOneByLogin(String login);

}
