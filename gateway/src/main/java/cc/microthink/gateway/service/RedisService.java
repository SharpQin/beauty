package cc.microthink.gateway.service;


import cc.microthink.common.dto.MenuDTO;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RedisService {

    private Logger logger = LoggerFactory.getLogger(RedisService.class);

    private final RedissonClient redissonClient;

    public RedisService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
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

    public String getRedisAllAuthorities() {
        String redisKey = "ALL_AUTHORITIES_KEY";
        RBucket<String> redisAuthorities = redissonClient.getBucket(redisKey);
        return redisAuthorities.get();
    }

    public List<MenuDTO> getAllMenu() {
        String allAuthString = getRedisAllAuthorities();
        if (StringUtils.isBlank(allAuthString)) {
            return Collections.emptyList();
        }

        return Arrays.stream(allAuthString.split(";")).map(menuStr -> {
            String[] menuElements = menuStr.split(",");
            return new MenuDTO(menuElements[0], menuElements[1], menuElements[2]);
        }).collect(Collectors.toList());
    }

}
