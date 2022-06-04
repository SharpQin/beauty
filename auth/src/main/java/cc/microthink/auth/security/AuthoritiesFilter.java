package cc.microthink.auth.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

public class AuthoritiesFilter implements WebFilter {

    private Logger logger = LoggerFactory.getLogger(AuthoritiesFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        //ServerHttpRequest request = exchange.getRequest();
        //AntPathRequestMatcher pathMatcher = new AntPathRequestMatcher("/api/roles", "GET", true);
        //boolean isMatched = pathMatcher.matches(request);

        //--
        RequestPath path = exchange.getRequest().getPath();
        //PathContainer path = org.springframework.http.server.PathContainer.parsePath(exchange.getRequest().getURI().getPath());

        //exchange.getRequest().getMethod().name();
        PathPatternParser pathParser = new PathPatternParser();
        pathParser.setMatchOptionalTrailingSeparator(true);
        pathParser.setCaseSensitive(true);
        PathPattern pathPattern = pathParser.parse("/api/roles/{id}");
        boolean isMatched = pathPattern.matches(path);

        logger.info("path: {}", path);
        logger.info("isMatched: {}", isMatched);

        return chain.filter(exchange);
    }
}
