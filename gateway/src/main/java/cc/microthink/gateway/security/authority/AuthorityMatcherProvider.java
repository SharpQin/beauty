package cc.microthink.gateway.security.authority;

import cc.microthink.common.dto.MenuDTO;
import cc.microthink.gateway.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.PathContainer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class AuthorityMatcherProvider implements IAuthorityMatcher {

    private Logger logger = LoggerFactory.getLogger(AuthorityMatcherProvider.class);

    private final RedisService redisService;

    //Indicator to switch two cache
    private final AtomicBoolean firstSelector = new AtomicBoolean(true);

    //First Cache
    private List<AuthPathPattern> authPathPatterns = new ArrayList<>(0);

    //Second Cache for switch
    private List<AuthPathPattern> authPathPatterns2 = new ArrayList<>(0);

    public AuthorityMatcherProvider(RedisService redisService) {
        this.redisService = redisService;
    }

    public void initial() {
        logger.info("---initial begin---");
        if (firstSelector.get()) {
            authPathPatterns = getAuthPathListFromRedis();
        }
        else {
            authPathPatterns2 = getAuthPathListFromRedis();
        }
        logger.info("---initial end---");
    }

    public void refresh() {
        if (this.firstSelector.get()) {
            logger.info("---refresh selector 2---");
            // using authPathPatterns currently, reset authPathPatterns2 and switch to authPathPatterns2
            this.authPathPatterns2 = getAuthPathListFromRedis();
            firstSelector.set(false);
        }
        else {
            logger.info("---refresh selector 1---");
            // using authPathPatterns2 currently, reset authPathPatterns and switch to authPathPatterns
            this.authPathPatterns = getAuthPathListFromRedis();
            this.firstSelector.set(true);
        }
    }

    private List<AuthPathPattern> getAuthPathListFromRedis() {
        List<MenuDTO> allMenu =  redisService.getAllMenu();
        List<AuthPathPattern> theList = new ArrayList<>(allMenu.size());

        PathPatternParser pathParser = new PathPatternParser();
        pathParser.setMatchOptionalTrailingSeparator(true);
        pathParser.setCaseSensitive(true);
        for(MenuDTO menu : allMenu) {
            PathPattern pathPattern = pathParser.parse(menu.getLink());
            theList.add(new AuthPathPattern(menu, pathPattern));
        }
        return theList;
    }

    @Override
    public String matchedAuth(PathContainer path, String method) {
        List<AuthPathPattern> theAuthList = firstSelector.get() ? this.authPathPatterns : this.authPathPatterns2;
        String matchedV = "";
        for (AuthPathPattern ap : theAuthList) {
            if (StringUtils.isNotEmpty(ap.matchedAuth(path, method))) {
                matchedV = ap.getAuthority();
                break;
            }
        }
        return matchedV;
    }
}
