package cc.microthink.auth.config;

import cc.microthink.auth.service.RedisService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Or some method: @EventListener(ApplicationReadyEvent.class)
 */

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final RedisService redisService;

    public ApplicationStartup(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        redisService.initialRedisAuthorities();
    }
}
