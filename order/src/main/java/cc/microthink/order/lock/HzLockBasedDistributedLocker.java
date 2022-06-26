package cc.microthink.order.lock;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.lock.FencedLock;

import java.util.concurrent.TimeUnit;

public class HzLockBasedDistributedLocker implements DistributedLocker {

    private HazelcastInstance hazelcastInstance;

    public HzLockBasedDistributedLocker(){
       this(3, 3);
    }

    public HzLockBasedDistributedLocker(int cpMemberCount, int groupSize){
        Config config = new Config();
        config.getCPSubsystemConfig().setCPMemberCount(cpMemberCount);
        config.getCPSubsystemConfig().setGroupSize(groupSize);
        this.hazelcastInstance = Hazelcast.newHazelcastInstance(config);
    }

    public HzLockBasedDistributedLocker(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public DistributedLock getLock(String key) {
        return wrapHzLock(key);
    }

    private DistributedLock wrapHzLock(String key){
        return new HzLock(key);
    }

    private class HzLock implements DistributedLock {

        private final FencedLock lock;

        public HzLock(String key) {
            this.lock = hazelcastInstance.getCPSubsystem().getLock(key);
        }

        @Override
        public void lock() {
            lock.lock();
        }

        @Override
        public boolean tryLock() {
            return lock.tryLock();
        }

        @Override
        public boolean tryLock(long timeout, TimeUnit unit) {
            try {
                return lock.tryLock(timeout, unit);
            } catch (Exception e) {
                throw new RuntimeException("Error while acquiring lock", e);
            }
        }

        @Override
        public boolean isLocked() {
            return lock.isLocked();
        }

        @Override
        public void unlock() {
            try {
                lock.unlock();
                //((DistributedObject) lock).destroy();
            } catch (Exception e){
                throw new RuntimeException("Error while releasing lock", e);
            }
        }
    }

}
