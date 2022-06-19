package cc.microthink.auth.security;

import cc.microthink.common.security.UserType;

public class MarketAuthenticationManagerMatcher implements AuthenticationManagerMatcher {
    @Override
    public boolean isMatched(Object details) {
        if (details != null && details instanceof UserType) {
            if (((UserType) details).isMarket()) {
                return true;
            }
        }
        return false;
    }
}
