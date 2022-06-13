package cc.microthink.auth.security;

/**
 * Match Strategy for AuthenticationManager
 */
public interface AuthenticationManagerMatcher {
    boolean isMatched(Object details);
}
