package cc.microthink.auth.service;

import cc.microthink.auth.IntegrationTest;
import cc.microthink.auth.domain.Permission;
import cc.microthink.auth.domain.Role;
import cc.microthink.auth.repository.RoleRepository;
import ch.qos.logback.core.net.SyslogOutputStream;
import org.junit.Assert;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

import java.util.Set;

@IntegrationTest
public class RoleServiceIT {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testRolePermission() {

        Flux<Role> roleFlux = roleRepository.findAllWithEagerRelationships();
        roleFlux.subscribe(role -> {

            String authorities =  role.getAuthorities();

            System.out.println(authorities);
        });
    }

}
