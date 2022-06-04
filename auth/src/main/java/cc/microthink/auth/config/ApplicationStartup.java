package cc.microthink.auth.config;

import cc.microthink.auth.domain.Menu;
import cc.microthink.auth.domain.Role;
import cc.microthink.auth.repository.MenuRepository;
import cc.microthink.auth.repository.RoleRepository;
import cc.microthink.auth.service.RedisService;
import cc.microthink.common.dto.MenuDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * Or some method: @EventListener(ApplicationReadyEvent.class)
 */

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final RedisService redisService;

    private final RoleRepository roleRepository;

    private final MenuRepository menuRepository;

    public ApplicationStartup(RedisService redisService, RoleRepository roleRepository, MenuRepository menuRepository) {
        this.redisService = redisService;
        this.roleRepository = roleRepository;
        this.menuRepository = menuRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        Flux<Role> roleFlux = roleRepository.findAllWithEagerRelationships();
        roleFlux.subscribe(role -> {
            redisService.saveRolePermissions(role);
        });

        Flux<Menu> menuFlux = menuRepository.findAll();
        menuFlux
            .filter(m -> StringUtils.isNotBlank(m.getAuthKey()))
            .map(menu -> new MenuDTO(menu.getLink(), menu.getMethod(), menu.getAuthKey()))
            .collectList()
            .subscribe(list -> redisService.saveAllMenu(list));

    }
}
