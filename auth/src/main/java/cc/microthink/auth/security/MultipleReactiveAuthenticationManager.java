package cc.microthink.auth.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class MultipleReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private List<ReactiveAuthenticationManager> authenticationManagers = new ArrayList<>(3);

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        Object details = authentication.getDetails();
        return authenticationManagers.stream().filter(am -> ((AuthenticationManagerMatcher)am).isMatched(details)).findFirst().get().authenticate(authentication);
    }

    public void setAuthenticationManagers(List<ReactiveAuthenticationManager> authenticationManagers) {
        this.authenticationManagers = authenticationManagers;
    }

    public void addAuthenticationManager(ReactiveAuthenticationManager am) {
        this.authenticationManagers.add(am);
    }
}
