package cc.microthink.auth.config;

import cc.microthink.auth.security.*;
import cc.microthink.auth.security.jwt.JWTFilter;
import cc.microthink.auth.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.ReferrerPolicyServerHttpHeadersWriter;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.Mode;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.zalando.problem.spring.webflux.advice.security.SecurityProblemSupport;
import tech.jhipster.config.JHipsterProperties;

import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity()
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration {

    private final JHipsterProperties jHipsterProperties;

    private final ReactiveUserDetailsService userDetailsService;

    private final ReactiveUserDetailsService marketUserDetailsService;

    private final TokenProvider tokenProvider;

    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(
        @Qualifier("userDetailsService") ReactiveUserDetailsService userDetailsService,
        @Qualifier("mkUserDetailsService") ReactiveUserDetailsService marketUserDetailsService,
        TokenProvider tokenProvider,
        JHipsterProperties jHipsterProperties,
        SecurityProblemSupport problemSupport
    ) {
        this.userDetailsService = userDetailsService;
        this.marketUserDetailsService = marketUserDetailsService;
        this.tokenProvider = tokenProvider;
        this.jHipsterProperties = jHipsterProperties;
        this.problemSupport = problemSupport;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        MatchedUserDetailsRepositoryReactiveAuthenticationManager managementAuthenticationManager = new MatchedUserDetailsRepositoryReactiveAuthenticationManager(
            userDetailsService
        );
        managementAuthenticationManager.setPasswordEncoder(passwordEncoder());
        managementAuthenticationManager.setMatcher(new ManagementAuthenticationManagerMatcher());

        MatchedUserDetailsRepositoryReactiveAuthenticationManager marketAuthenticationManager = new MatchedUserDetailsRepositoryReactiveAuthenticationManager(
            marketUserDetailsService
        );
        marketAuthenticationManager.setPasswordEncoder(passwordEncoder());
        marketAuthenticationManager.setMatcher(new MarketAuthenticationManagerMatcher());

        MultipleReactiveAuthenticationManager multipleAuthManager = new MultipleReactiveAuthenticationManager();
        multipleAuthManager.addAuthenticationManager(managementAuthenticationManager);
        multipleAuthManager.addAuthenticationManager(marketAuthenticationManager);

        return multipleAuthManager;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        // @formatter:off
        http
            .securityMatcher(new NegatedServerWebExchangeMatcher(new OrServerWebExchangeMatcher(
                pathMatchers("/app/**", "/i18n/**", "/content/**", "/swagger-ui/**", "/v3/api-docs/**", "/test/**"),
                pathMatchers(HttpMethod.OPTIONS, "/**")
            )))
            .csrf()
                .disable()
            .addFilterAt(new JWTFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
            .authenticationManager(reactiveAuthenticationManager())    //TODO note: check?
            .exceptionHandling()
                .accessDeniedHandler(problemSupport)
                .authenticationEntryPoint(problemSupport)
        .and()
            .headers()
            .contentSecurityPolicy(jHipsterProperties.getSecurity().getContentSecurityPolicy())
            .and()
                .referrerPolicy(ReferrerPolicyServerHttpHeadersWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
            .and()
                .permissionsPolicy().policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()")
            .and()
                .frameOptions().mode(Mode.DENY)
        .and()
            .requestCache()
            .requestCache(NoOpServerRequestCache.getInstance())
        .and()
            .authorizeExchange()

            .pathMatchers("/mki/authenticate").permitAll()
            .pathMatchers("/mki/register").permitAll()
            .pathMatchers("/mki/activate").permitAll()
            .pathMatchers("/mki/**").authenticated()

            .pathMatchers("/api/authenticate").permitAll()
            .pathMatchers("/api/account/reset-password/init").permitAll()
            .pathMatchers("/api/account/reset-password/finish").permitAll()
            .pathMatchers("/api/auth-info").permitAll()

            .pathMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .pathMatchers("/api/**").authenticated()


            .pathMatchers("/management/health").permitAll()
            .pathMatchers("/management/health/**").permitAll()
            .pathMatchers("/management/info").permitAll()
            .pathMatchers("/management/prometheus").permitAll()
            .pathMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN);


        // @formatter:on
        return http.build();
    }
}
