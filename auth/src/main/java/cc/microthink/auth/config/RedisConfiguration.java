package cc.microthink.auth.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfiguration {

    private final ApplicationProperties applicationProperties;

    public RedisConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() {
        Config config = new Config();
        Codec codec = JsonJacksonCodec.INSTANCE;
        config.setCodec(codec);
        config.useSingleServer().setAddress("redis://localhost:6379");

//        config.useClusterServers()
//            .addNodeAddress("redis://127.0.0.1:7004", "redis://127.0.0.1:7001");
        return Redisson.create(config);
    }

    @Bean
    CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<String, CacheConfig>();

        // create "testMap" cache with ttl = 24 minutes and maxIdleTime = 12 minutes
        config.put("testMap", new CacheConfig(24*60*1000, 12*60*1000));
        return new RedissonSpringCacheManager(redissonClient, config);
    }
}
