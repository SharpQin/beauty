package cc.microthink.auth.service;

import cc.microthink.auth.domain.MKUser;
import cc.microthink.auth.message.out.MessageOutService;
import cc.microthink.auth.repository.MKUserRepository;
import cc.microthink.auth.security.SecurityUtils;
import cc.microthink.auth.service.dto.MKUserDTO;
import cc.microthink.common.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.jhipster.security.RandomUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Service
public class MKUserService {

    private final Logger log = LoggerFactory.getLogger(MKUserService.class);

    private final MKUserRepository mkUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final MessageOutService messageOutService;

    public MKUserService(MKUserRepository mkUserRepository, PasswordEncoder passwordEncoder, MessageOutService messageOutService) {
        this.mkUserRepository = mkUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageOutService = messageOutService;
    }

    @Transactional
    public Mono<MKUser> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return mkUserRepository
            .findOneByActivationKey(key)
            .flatMap(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                return saveUser(user);
            })
            .doOnNext(user -> log.debug("Activated user: {}", user));
    }

    @Transactional
    public Mono<MKUser> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return mkUserRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS)))
            .publishOn(Schedulers.boundedElastic())
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                return user;
            })
            .flatMap(this::saveUser);
    }

    @Transactional
    public Mono<MKUser> requestPasswordReset(String mail) {
        return mkUserRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(MKUser::isActivated)
            .publishOn(Schedulers.boundedElastic())
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                return user;
            })
            .flatMap(this::saveUser);
    }

    @Transactional
    public Mono<MKUser> registerUser(MKUserDTO userDTO, String password) {
        return mkUserRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .flatMap(existingUser -> {
                if (!existingUser.isActivated()) {
                    return mkUserRepository.delete(existingUser);
                } else {
                    return Mono.error(new UsernameAlreadyUsedException());
                }
            })
            .then(mkUserRepository.findOneByEmailIgnoreCase(userDTO.getEmail()))
            .flatMap(existingUser -> {
                if (!existingUser.isActivated()) {
                    return mkUserRepository.delete(existingUser);
                } else {
                    return Mono.error(new EmailAlreadyUsedException());
                }
            })
            .publishOn(Schedulers.boundedElastic())
            .then(
                Mono.fromCallable(() -> {
                    MKUser newUser = new MKUser();
                    String encryptedPassword = passwordEncoder.encode(password);
                    newUser.setLogin(userDTO.getLogin().toLowerCase());
                    // new user gets initially a generated password
                    newUser.setPassword(encryptedPassword);
                    newUser.setNickName(userDTO.getNickName());

                    if (userDTO.getEmail() != null) {
                        newUser.setEmail(userDTO.getEmail().toLowerCase());
                    }
                    if (userDTO.getPhone() != null) {
                        newUser.setPhone(userDTO.getPhone());
                    }
                    newUser.setRole(AuthoritiesConstants.MARKET);
                    newUser.setImageUrl(userDTO.getImageUrl());
                    newUser.setLangKey(userDTO.getLangKey());
                    // new user is not active Note: Set active as default
                    newUser.setActivated(true);  //false
                    // new user gets registration key
                    newUser.setActivationKey(RandomUtil.generateActivationKey());
                    return newUser;
                })
            )
            .flatMap(this::saveUser)
            .doOnNext(user -> {
                log.debug("Created Information for User: {}", user);
                //Email notificaiton
                messageOutService.sendEmailNotify(user,"Create new User:" + user.getLogin());
            });
    }

    @Transactional
    public Mono<Void> updateUser(String nickName, String email, String langKey, String imageUrl) {
        return SecurityUtils
            .getCurrentUserLogin()
            .flatMap(mkUserRepository::findOneByLogin)
            .flatMap(user -> {
                user.setNickName(nickName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                return saveUser(user);
            })
            .doOnNext(user -> log.debug("Changed Information for User: {}", user))
            .then();
    }

    @Transactional
    public Mono<MKUser> saveUser(MKUser user) {
        return mkUserRepository.save(user);
    }

    @Transactional
    public Mono<Void> changePassword(String currentClearTextPassword, String newPassword) {
        return SecurityUtils
            .getCurrentUserLogin()
            .flatMap(mkUserRepository::findOneByLogin)
            .publishOn(Schedulers.boundedElastic())
            .map(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                return user;
            })
            .flatMap(this::saveUser)
            .doOnNext(user -> log.debug("Changed password for User: {}", user))
            .then();
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        removeNotActivatedUsersReactively().blockLast();
    }

    @Transactional
    public Flux<MKUser> removeNotActivatedUsersReactively() {
        return mkUserRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(
                LocalDateTime.ofInstant(Instant.now().minus(3, ChronoUnit.DAYS), ZoneOffset.UTC)
            )
            .flatMap(user -> mkUserRepository.delete(user).thenReturn(user))
            .doOnNext(user -> log.debug("Deleted User: {}", user));
    }

    @Transactional(readOnly = true)
    public Mono<MKUser> getLoginedUser() {
        return SecurityUtils.getCurrentUserLogin().flatMap(mkUserRepository::findOneByLogin);
    }

}
