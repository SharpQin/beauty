package cc.microthink.auth.security;

import cc.microthink.auth.domain.MKUser;
import cc.microthink.auth.repository.MKUserRepository;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component("mkUserDetailsService")
public class MKUserDetailsService implements ReactiveUserDetailsService {

    private final Logger log = LoggerFactory.getLogger(MKUserDetailsService.class);

    private final MKUserRepository userRepository;

    public MKUserDetailsService(MKUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String login) {
        log.debug("Authenticating {}", login);

        if (new EmailValidator().isValid(login, null)) {
            return userRepository.findOneByEmailIgnoreCase(login)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User with email " + login + " was not found in the database")))
                .map(user -> createSpringSecurityUser(login, user));
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        return userRepository
            .findOneByLogin(lowercaseLogin)
            .switchIfEmpty(Mono.error(new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database")))
            .map(user -> createSpringSecurityUser(lowercaseLogin, user));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, MKUser user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(1);
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), grantedAuthorities);
    }
}
