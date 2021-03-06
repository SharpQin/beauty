package cc.microthink.auth.service;

import cc.microthink.auth.domain.Authority;
import cc.microthink.auth.domain.Role;
import cc.microthink.auth.message.out.MessageOutService;
import cc.microthink.auth.repository.AuthorityRepository;
import cc.microthink.auth.repository.RoleRepository;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RedisService {

    private Logger logger = LoggerFactory.getLogger(RedisService.class);

    private final RedissonClient redissonClient;

    private final RoleRepository roleRepository;

    private final AuthorityRepository authorityRepository;

    public RedisService(RedissonClient redissonClient, RoleRepository roleRepository, AuthorityRepository authorityRepository) {
        this.redissonClient = redissonClient;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    public void initialRedisAuthorities() {
        this.refreshAllRoles();
        refreshAllAuthorities();
    }

    public void refreshAllRoles() {
        Flux<Role> roleFlux = roleRepository.findAll();
        //.filter(role -> StringUtils.isNotBlank(role.getAuthorities()))
        roleFlux.subscribe(role -> {
            saveRedisRoleAuthorities(role);
        });
    }

    public void refreshAllAuthorities() {
        Flux<Authority> menuFlux = authorityRepository.findAll();
        menuFlux
            .filter(auth -> StringUtils.isNotBlank(auth.getAuthKey()))
            .map(auth -> String.format("%s,%s,%s", auth.getLink(), auth.getMethod(), auth.getAuthKey()))
            .reduce((str1, str2) -> str1 + ";" + str2)
            .subscribe(allAuthStr -> {
                saveRedisAllAuthorities(allAuthStr);
            });
    }

    public void saveRedisRoleAuthorities(Role role) {
        /*

        RAtomicLong myLong = redissonClient.getAtomicLong("MyLong");
        myLong.set(1234);

        RBucket<String> bucket = redissonClient.getBucket("bucket");
        bucket.set("MyBucketString");
        String vlaue = bucket.get();

        RSet<String> permissionSet = redissonClient.getSet(role.getName());
        */

        if (StringUtils.isBlank(role.getAuthorities())) {
            logger.warn("saveRedisRoleAuthorities: Empty authorities of role:{}", role.getName());
            return;
        }

        logger.info("saveRedisRoleAuthorities: redis key:{}", "RSHARED_" + role.getName());

        String redisKey = "ROLE_KEY_" + role.getName();
        RBucket<String> redisRole = redissonClient.getBucket(redisKey);
        redisRole.set(role.getAuthorities());
        logger.info("saveRedisRoleAuthorities: role:{} and role Authorities:{}", role.getName(), role.getAuthorities());
    }

    public void removeRedisRole(String roleName) {
        logger.debug("removeRedisRole: roleName:{}", roleName);
        String redisKey = "ROLE_KEY_" + roleName;
        RBucket<String> redisRole = redissonClient.getBucket(redisKey);
        boolean deleteResult = redisRole.delete();
        if (!deleteResult) {
            logger.warn("removeRedisRole: Fail to remove redis key:{}", redisKey);
        }
        else {
            logger.warn("removeRedisRole: Success to remove redis key:{}", redisKey);
        }
    }

    public String getRedisRoleAuthorities(String roleName) {
        String redisKey = "ROLE_KEY_" + roleName;
        RBucket<String> redisRole = redissonClient.getBucket(redisKey);
        return redisRole.get();
    }

    public Set<String> getRoleAuthorities(String roleName) {
        String authString = getRedisRoleAuthorities(roleName);
        if (StringUtils.isBlank(authString)) {
            return Collections.emptySet();
        }
        return Arrays.stream(authString.split(",")).collect(Collectors.toSet());
    }

    public void saveRedisAllAuthorities(String allAuthStr) {
        if (StringUtils.isBlank(allAuthStr)) {
            return;
        }

        String redisKey = "ALL_AUTHORITIES_KEY";
        RBucket<String> redisAuthorities = redissonClient.getBucket(redisKey);
        redisAuthorities.set(allAuthStr);
        logger.info("saveRedisAllAuthorities: allAuthStr:{}", allAuthStr);

        /*
        RList<MenuDTO> redisList = redissonClient.getList("RSHARED_ALL_MENU");
        if (!redisList.isExists()) {
            menus.forEach(menu -> {
                redisList.add(menu);
                logger.info("saveAllMenu: add menu:{}", menu);
            });
        }
        */
    }

    public String getRedisAllAuthorities() {
        String redisKey = "ALL_AUTHORITIES_KEY";
        RBucket<String> redisAuthorities = redissonClient.getBucket(redisKey);
        return redisAuthorities.get();
    }

}
