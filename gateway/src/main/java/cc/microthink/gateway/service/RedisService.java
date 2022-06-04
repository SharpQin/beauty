package cc.microthink.gateway.service;


import cc.microthink.common.dto.MenuDTO;
import org.redisson.api.RList;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private Logger logger = LoggerFactory.getLogger(RedisService.class);

    private final RedissonClient redissonClient;

    public RedisService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public RSet<String> getRolePermissions(String roleName) {
        return redissonClient.getSet(roleName);
    }

    public RList<MenuDTO> getAllMenu() {
        RList<MenuDTO> result =  redissonClient.getList("RSHARED_ALL_MENU");
        result.forEach(menu -> {
            logger.info("getAllMenu: get menu:{}", menu);
        });
        return result;
    }

}
