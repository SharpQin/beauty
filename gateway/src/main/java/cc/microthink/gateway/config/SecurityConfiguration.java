package cc.microthink.gateway.config;

import cc.microthink.gateway.security.authority.AuthorityMatcherProvider;
import cc.microthink.gateway.security.authority.PathAuthorityReactiveAuthorizationManager;
import cc.microthink.gateway.security.jwt.JWTFilter;
import cc.microthink.gateway.security.jwt.TokenProvider;
import cc.microthink.gateway.service.RedisService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationWebFilter;
import org.springframework.security.web.server.header.ReferrerPolicyServerHttpHeadersWriter;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.Mode;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.zalando.problem.spring.webflux.advice.security.SecurityProblemSupport;
import tech.jhipster.config.JHipsterProperties;

import java.util.List;

import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration {

    private final JHipsterProperties jHipsterProperties;

    private final TokenProvider tokenProvider;

    private final SecurityProblemSupport problemSupport;

    private final RedisService redisService;

    private final AuthorityMatcherProvider authorityMatcherProvider;

    public SecurityConfiguration(
        TokenProvider tokenProvider,
        JHipsterProperties jHipsterProperties,
        SecurityProblemSupport problemSupport,
        RedisService redisService,
        AuthorityMatcherProvider authorityMatcherProvider) {
        this.tokenProvider = tokenProvider;
        this.jHipsterProperties = jHipsterProperties;
        this.problemSupport = problemSupport;
        this.redisService = redisService;
        this.authorityMatcherProvider = authorityMatcherProvider;
    }

    @Bean
    public ReactiveAuthorizationManager reactiveAuthenticationManager() {
        List<String> permitUrls = List.of(
            "/services/auth/mki/authenticate",
            "/services/auth/api/authenticate",
            "/services/auth/api/register",
            "/services/auth/api/activate",
            "/services/auth/api/account/reset-password/init",
            "/services/auth/api/account/reset-password/finish"
            );
        return new PathAuthorityReactiveAuthorizationManager(authorityMatcherProvider, permitUrls);
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
            //.addFilterAfter(new AppendRoleAuthoritiesFilter(redisService), SecurityWebFiltersOrder.HTTP_BASIC)
            .addFilterAt(new AuthorizationWebFilter(reactiveAuthenticationManager()), SecurityWebFiltersOrder.AUTHORIZATION)
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
                .frameOptions().mode(Mode.DENY);
        // @formatter:on
        return http.build();
    }
}
