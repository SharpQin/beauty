package cc.microthink.order.lock;

import java.util.concurrent.TimeUnit;

public interface DistributedLock {

    void lock();

    boolean tryLock();

    boolean tryLock(long timeout, TimeUnit unit);

    boolean isLocked();

    void release();

}
