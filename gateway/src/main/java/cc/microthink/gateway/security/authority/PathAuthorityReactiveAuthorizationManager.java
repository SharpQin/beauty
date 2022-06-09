package cc.microthink.gateway.security.authority;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorityAuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class PathAuthorityReactiveAuthorizationManager implements ReactiveAuthorizationManager<ServerWebExchange> {

    private Logger logger = LoggerFactory.getLogger(PathAuthorityReactiveAuthorizationManager.class);

    private final AuthorityMatcherProvider authorityMatcherProvider;

    private final List<PathPattern> permitPaths = new ArrayList<>();

    public PathAuthorityReactiveAuthorizationManager(AuthorityMatcherProvider authorityMatcherProvider, List<String> permitUrls) {
        this.authorityMatcherProvider = authorityMatcherProvider;

        PathPatternParser pathParser = new PathPatternParser();
        pathParser.setMatchOptionalTrailingSeparator(true);
        pathParser.setCaseSensitive(true);
        for (String url : permitUrls) {
            PathPattern pathPattern = pathParser.parse(url);
            permitPaths.add(pathPattern);
        }
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, ServerWebExchange serverWebExchange) {
        //Check if it's permitted url first.
        if (permitPaths.stream().anyMatch(p-> p.matches(serverWebExchange.getRequest().getPath()))) {
            logger.info("check:Permit path:{}", serverWebExchange.getRequest().getPath());
            return Mono.just(new AuthorizationDecision(true));
        }

        String expectedAuthority = authorityMatcherProvider.matchedAuth(serverWebExchange.getRequest().getPath(), serverWebExchange.getRequest().getMethod().name());

        //
        if (StringUtils.isEmpty(expectedAuthority)) {
            logger.warn("check:Not found authority configure for path:{}", serverWebExchange.getRequest().getPath());
            return Mono.just(new AuthorizationDecision(false));
        }

        if (logger.isInfoEnabled()) {
            logger.info("### check: path:{}, expectedAuthority:{}", serverWebExchange.getRequest().getPath(), expectedAuthority);
        }
        return authentication.filter(Authentication::isAuthenticated)
            .flatMapIterable(Authentication::getAuthorities)
            .map(GrantedAuthority::getAuthority)
            .any(grantedAuth -> expectedAuthority.equals(grantedAuth))
            .map((granted) -> ((AuthorizationDecision) new AuthorityAuthorizationDecision(granted, AuthorityUtils.createAuthorityList(expectedAuthority))))
            .defaultIfEmpty(new AuthorityAuthorizationDecision(false, AuthorityUtils.createAuthorityList(expectedAuthority)));
    }
}
