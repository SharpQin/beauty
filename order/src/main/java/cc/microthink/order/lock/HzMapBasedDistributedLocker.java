package cc.microthink.order.lock;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.concurrent.TimeUnit;

public class HzMapBasedDistributedLocker implements DistributedLocker {

    private IMap txLockMap;

    public HzMapBasedDistributedLocker(){
        this(Hazelcast.newHazelcastInstance());
    }

    public HzMapBasedDistributedLocker(HazelcastInstance hazelcastInstance){
        txLockMap = hazelcastInstance.getMap("txLockMap");
    }

    @Override
    public DistributedLock getLock(String key) {
        return new HzMapBasedLock(key);
    }

    private class HzMapBasedLock implements DistributedLock{

        private final String key;

        public HzMapBasedLock(String key) {
            this.key = key;
        }

        @Override
        public void lock() {
            txLockMap.lock(key);
        }

        @Override
        public boolean tryLock() {
            return txLockMap.tryLock(key);
        }

        @Override
        public boolean tryLock(long timeout, TimeUnit unit) {
            try {
                return txLockMap.tryLock(key, timeout, unit);
            } catch (Exception e) {
                throw new RuntimeException("Error while acquiring lock", e);
            }
        }

        @Override
        public boolean isLocked() {
            return txLockMap.isLocked(key);
        }

        @Override
        public void unlock() {
            try {
                txLockMap.unlock(key);
            } catch (Exception e){
                throw new RuntimeException("Error while releasing lock", e);
            }
        }
    }


}
