package cc.microthink.gateway.security.authority;

import org.springframework.http.server.PathContainer;

public interface IAuthorityMatcher {

    String matchedAuth(PathContainer path, String method);
}
