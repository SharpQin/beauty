package cc.microthink.gateway.security.authority;

import cc.microthink.gateway.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AppendRoleAuthoritiesFilter implements WebFilter {

    private Logger logger = LoggerFactory.getLogger(AppendRoleAuthoritiesFilter.class);

    private final RedisService redisService;

    public AppendRoleAuthoritiesFilter(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        logger.info("------AppendRoleAuthoritiesFilter.filter------");
        return ReactiveSecurityContextHolder.getContext().filter(securityContext -> securityContext.getAuthentication() != null).map(securityContext -> {
            logger.info("======AppendRoleAuthoritiesFilter.map======");
            Authentication authentication = securityContext.getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Set<String> allRoleAuthorities = new HashSet<>(10);
            for (GrantedAuthority auth : authorities) {
                Set<String> authoritiesOfRole = redisService.getRoleAuthorities(auth.getAuthority());
                logger.info("filter: role:{}, authoritiesOfRole:{}", auth.getAuthority(), authoritiesOfRole);
                if (!authoritiesOfRole.isEmpty()) {
                    allRoleAuthorities.addAll(authoritiesOfRole);
                }
            }
            if (!allRoleAuthorities.isEmpty()) {
                authorities.addAll((Collection)allRoleAuthorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
            }
            return securityContext;
        })
            .as(context -> chain.filter(exchange))
            .switchIfEmpty(chain.filter(exchange));
//        exchange.getApplicationContext().
//        ReactiveSecurityContextHolder.getContext().
//
//        return null;
    }
}
