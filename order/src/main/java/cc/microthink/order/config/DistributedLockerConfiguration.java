package cc.microthink.order.config;

import cc.microthink.order.lock.DistributedLocker;
import cc.microthink.order.lock.HzMapBasedDistributedLocker;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DistributedLockerConfiguration {

    private Logger log = LoggerFactory.getLogger("DistributedLockerConfiguration");

    @Bean
    public DistributedLocker distributedLocker(HazelcastInstance hazelcastInstance) {
        log.debug("### distributedLocker: name:{}, hashcode:{}, obj:{}", hazelcastInstance.getName(), hazelcastInstance.hashCode(), hazelcastInstance);
        return new HzMapBasedDistributedLocker(hazelcastInstance);
    }

}
