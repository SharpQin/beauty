package cc.microthink.gateway.config;

import cc.microthink.gateway.security.authority.AuthorityMatcherProvider;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Or some method: @EventListener(ApplicationReadyEvent.class)
 */

//@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final AuthorityMatcherProvider authorityMatcherProvider;

    public ApplicationStartup(AuthorityMatcherProvider authorityMatcherProvider) {
        this.authorityMatcherProvider = authorityMatcherProvider;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        authorityMatcherProvider.initial();
    }
}
