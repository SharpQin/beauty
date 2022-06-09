package cc.microthink.gateway.security.authority;

import cc.microthink.common.dto.MenuDTO;
import cc.microthink.gateway.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorityMatcherProvider implements IAuthorityMatcher {

    private Logger logger = LoggerFactory.getLogger(AuthorityMatcherProvider.class);

    private final RedisService redisService;

    private List<AuthPathPattern> authPathPatterns;

    public AuthorityMatcherProvider(RedisService redisService) {
        this.redisService = redisService;
    }

    public void initial() {
        logger.info("---initial begin---");
        List<MenuDTO> allMenu =  redisService.getAllMenu();
        authPathPatterns = new ArrayList<>(allMenu.size());

        PathPatternParser pathParser = new PathPatternParser();
        pathParser.setMatchOptionalTrailingSeparator(true);
        pathParser.setCaseSensitive(true);

        for(MenuDTO menu : allMenu) {
            PathPattern pathPattern = pathParser.parse(menu.getLink());
            authPathPatterns.add(new AuthPathPattern(menu, pathPattern));
        }

        logger.info("---initial end---");
    }

    @Override
    public String matchedAuth(PathContainer path, String method) {

        if (authPathPatterns == null) {
            //Something wrong
            return "";
        }
        String matchedV = "";
        for (AuthPathPattern ap : authPathPatterns) {
            if (StringUtils.isNotEmpty(ap.matchedAuth(path, method))) {
                matchedV = ap.getAuthority();
                break;
            }
        }
        return matchedV;
    }
}
