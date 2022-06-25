package cc.microthink.order.lock;

public interface DistributedLocker {

    DistributedLock getLock(String key);

}

