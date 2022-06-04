package cc.microthink.auth.service;

import cc.microthink.auth.domain.Role;
import cc.microthink.common.dto.MenuDTO;
import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisService {

    private Logger logger = LoggerFactory.getLogger(RedisService.class);

    private final RedissonClient redissonClient;

    public RedisService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void saveRolePermissions(Role role) {
        /*RAtomicLong myLong = redissonClient.getAtomicLong("MyLong");
        myLong.set(1234);

        RBucket<String> bucket = redissonClient.getBucket("bucket");
        bucket.set("MyBucketString");
        String vlaue = bucket.get();*/

        logger.info("saveRolePermissions: redis key:{}", "RSHARED_" + role.getName());
        RSet<String> permissionSet = redissonClient.getSet(role.getName());
        role.getPermissions().forEach(permission -> {
            permissionSet.add(permission.getAuthKey());
            logger.info("saveRolePermissions: add permission auth key:{}", permission.getAuthKey());
        });
    }

    public RSet<String> getRolePermissions(String roleName) {
        return redissonClient.getSet(roleName);
    }

    public void saveAllMenu(List<MenuDTO> menus) {
        if (menus == null || menus.isEmpty()) {
            return;
        }
        RList<MenuDTO> redisList = redissonClient.getList("RSHARED_ALL_MENU");
        if (!redisList.isExists()) {
            menus.forEach(menu -> {
                redisList.add(menu);
                logger.info("saveAllMenu: add menu:{}", menu);
            });
        }
    }

}
