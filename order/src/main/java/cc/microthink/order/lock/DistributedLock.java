package cc.microthink.order.lock;

import java.util.concurrent.TimeUnit;

/**
 * A distributed lock with a unique key is a tool for controlling access to a shared resource by multiple distributed app.
 */
public interface DistributedLock {

    /**
     * Acquires the lock. If the lock is not available, the current thread until the lock has been acquired.
     * The distributed lock acquired by a thread has to be released by same thread only.
     */
    void lock();

    /**
     * This is a non-blocking version of lock() method; it attempts to acquire the lock immediately, return true if locking succeeds.
     * @return true: successful | false: failure
     */
    boolean tryLock();

    /**
     * Acquires the lock. Blocks until the lock is available or timeout is expired.
     * @param timeout
     * @param unit
     * @return
     */
    boolean tryLock(long timeout, TimeUnit unit);

    /**
     * Checks if current thread has already acquire the lock.
     * @return
     */
    boolean isLocked();

    /**
     * Releases the lock. This method has to be called by same thread as which has acquired the lock.
     */
    void unlock();

}
