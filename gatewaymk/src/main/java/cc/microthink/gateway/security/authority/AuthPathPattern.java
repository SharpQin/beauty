package cc.microthink.gateway.security.authority;

import cc.microthink.common.dto.MenuDTO;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;

public class AuthPathPattern implements IAuthorityMatcher {

    private final MenuDTO menuDTO;

    private final PathPattern pathPattern;

    public AuthPathPattern(MenuDTO menuDTO, PathPattern pathPattern) {
        this.menuDTO = menuDTO;
        this.pathPattern = pathPattern;
    }

    @Override
    public String matchedAuth(PathContainer path, String method) {
        if (menuDTO.getMethod() == null || menuDTO.getMethod().length() == 0) {
            return this.pathPattern.matches(path) ? getAuthority() : "";
        }
        return this.pathPattern.matches(path) && method.equalsIgnoreCase(menuDTO.getMethod()) ? getAuthority() : "";
    }

    public String getAuthority() {
        return this.menuDTO.getAuthKey();
    }
}
