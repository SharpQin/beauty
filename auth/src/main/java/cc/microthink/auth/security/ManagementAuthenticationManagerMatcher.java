package cc.microthink.auth.security;

import cc.microthink.common.security.UserType;

public class ManagementAuthenticationManagerMatcher implements AuthenticationManagerMatcher {
    @Override
    public boolean isMatched(Object details) {
        if (details != null && details instanceof UserType) {
            if (((UserType) details).isManager()) {
                return true;
            }
        }
        return false;
    }
}
