package cc.microthink.auth.service;

import cc.microthink.auth.domain.Authority;
import cc.microthink.auth.domain.Role;
import cc.microthink.auth.repository.AuthorityRepository;
import cc.microthink.auth.repository.RoleRepository;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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
        Flux<Role> roleFlux = roleRepository.findAll();
        //.filter(role -> StringUtils.isNotBlank(role.getAuthorities()))
        roleFlux.subscribe(role -> {
            saveRedisRoleAuthorities(role);
        });

        StringBuilder authoritiesSb = new StringBuilder(128);
        Flux<Authority> menuFlux = authorityRepository.findAll();
        menuFlux
            .filter(auth -> StringUtils.isNotBlank(auth.getAuthKey()))
            .map(auth -> String.format("%s,%s,%s", auth.getLink(), auth.getMethod(), auth.getAuthKey()))
            .reduce((str1, str2) -> str1 + ";" + str2)
            .subscribe(allAuthStr -> saveRedisAllAuthorities(allAuthStr));
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

    public String getRedisRoleAuthorities(String roleName) {
        String redisKey = "ROLE_KEY_" + roleName;
        RBucket<String> redisRole = redissonClient.getBucket(redisKey);
        return redisRole.get();
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
