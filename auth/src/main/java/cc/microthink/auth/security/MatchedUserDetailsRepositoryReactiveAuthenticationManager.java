package cc.microthink.auth.security;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class MatchedUserDetailsRepositoryReactiveAuthenticationManager extends UserDetailsRepositoryReactiveAuthenticationManager implements AuthenticationManagerMatcher {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private ReactiveUserDetailsPasswordService userDetailsPasswordService;

    private Scheduler scheduler = Schedulers.boundedElastic();

    private UserDetailsChecker preAuthenticationChecks = this::defaultPreAuthenticationChecks;

    private UserDetailsChecker postAuthenticationChecks = this::defaultPostAuthenticationChecks;

    private AuthenticationManagerMatcher matcher;

    public MatchedUserDetailsRepositoryReactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    public boolean isMatched(Object details) {
        if (this.matcher == null) {
            return false;
        }
        return this.matcher.isMatched(details);
    }

    public void setMatcher(AuthenticationManagerMatcher matcher) {
        this.matcher = matcher;
    }

    private void defaultPreAuthenticationChecks(UserDetails user) {
        if (!user.isAccountNonLocked()) {
            this.logger.debug("User account is locked");
            throw new LockedException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked",
                "User account is locked"));
        }
        if (!user.isEnabled()) {
            this.logger.debug("User account is disabled");
            throw new DisabledException(
                this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
        }
        if (!user.isAccountNonExpired()) {
            this.logger.debug("User account is expired");
            throw new AccountExpiredException(this.messages
                .getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"));
        }
    }

    private void defaultPostAuthenticationChecks(UserDetails user) {
        if (!user.isCredentialsNonExpired()) {
            this.logger.debug("User account credentials have expired");
            throw new CredentialsExpiredException(this.messages.getMessage(
                "AbstractUserDetailsAuthenticationProvider.credentialsExpired", "User credentials have expired"));
        }
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String username = authentication.getName();
        String presentedPassword = (String) authentication.getCredentials();
        // @formatter:off
        return retrieveUser(username)
            .doOnNext(this.preAuthenticationChecks::check)
            .publishOn(this.scheduler)
            .filter((userDetails) -> this.passwordEncoder.matches(presentedPassword, userDetails.getPassword()))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new BadCredentialsException("Invalid Credentials"))))
            .flatMap((userDetails) -> upgradeEncodingIfNecessary(userDetails, presentedPassword))
            .doOnNext(this.postAuthenticationChecks::check)
            .map(userDetails -> {
                UsernamePasswordAuthenticationToken authToken = createUsernamePasswordAuthenticationToken(userDetails);
                authToken.setDetails(authentication.getDetails());
                return authToken;
            });
        // @formatter:on
    }

    private Mono<UserDetails> upgradeEncodingIfNecessary(UserDetails userDetails, String presentedPassword) {
        boolean upgradeEncoding = this.userDetailsPasswordService != null
            && this.passwordEncoder.upgradeEncoding(userDetails.getPassword());
        if (upgradeEncoding) {
            String newPassword = this.passwordEncoder.encode(presentedPassword);
            return this.userDetailsPasswordService.updatePassword(userDetails, newPassword);
        }
        return Mono.just(userDetails);
    }

    private UsernamePasswordAuthenticationToken createUsernamePasswordAuthenticationToken(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
            userDetails.getAuthorities());
    }

    /**
     * The {@link PasswordEncoder} that is used for validating the password. The default
     * is {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}
     * @param passwordEncoder the {@link PasswordEncoder} to use. Cannot be null
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Sets the {@link Scheduler} used by the
     * {@link UserDetailsRepositoryReactiveAuthenticationManager}. The default is
     * {@code Schedulers.newParallel(String)} because modern password encoding is a CPU
     * intensive task that is non blocking. This means validation is bounded by the number
     * of CPUs. Some applications may want to customize the {@link Scheduler}. For
     * example, if users are stuck using the insecure
     * {@link org.springframework.security.crypto.password.NoOpPasswordEncoder} they might
     * want to leverage {@code Schedulers.immediate()}.
     * @param scheduler the {@link Scheduler} to use. Cannot be null.
     * @since 5.0.6
     */
    public void setScheduler(Scheduler scheduler) {
        Assert.notNull(scheduler, "scheduler cannot be null");
        this.scheduler = scheduler;
    }

    /**
     * Sets the service to use for upgrading passwords on successful authentication.
     * @param userDetailsPasswordService the service to use
     */
    public void setUserDetailsPasswordService(ReactiveUserDetailsPasswordService userDetailsPasswordService) {
        this.userDetailsPasswordService = userDetailsPasswordService;
    }

    /**
     * Sets the strategy which will be used to validate the loaded <tt>UserDetails</tt>
     * object after authentication occurs.
     * @param postAuthenticationChecks The {@link UserDetailsChecker}
     * @since 5.2
     */
    public void setPostAuthenticationChecks(UserDetailsChecker postAuthenticationChecks) {
        Assert.notNull(this.postAuthenticationChecks, "postAuthenticationChecks cannot be null");
        this.postAuthenticationChecks = postAuthenticationChecks;
    }

    /**
     * @since 5.5
     */
    @Override
    public void setMessageSource(MessageSource messageSource) {
        Assert.notNull(messageSource, "messageSource cannot be null");
        this.messages = new MessageSourceAccessor(messageSource);
    }
}
