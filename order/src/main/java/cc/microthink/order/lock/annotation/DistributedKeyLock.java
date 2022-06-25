package cc.microthink.order.lock.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DistributedKeyLock {

    GetLockType lockType() default GetLockType.BLOCK;

    boolean usePrefix() default true;

    /**
     * Constant string key: Don't start with '#'. e.g. mydistributedkey
     * Dynamic object's property key: start with '#'. e.g. #objproperty, #objproperty.prop1.prop2
     */
    String key();

    String prefix() default  "DIS_LOCK_";

    long timeout() default 3000;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * FailBehavior.NON: Won't execute target method and return null when it's fail to get a distributed lock.
     * FailBehavior.EXCEPTION: Throw DistributedLockException when it's fail to get a distributed lock.
     */
    FailBehavior lockFailBehavior() default FailBehavior.NON;

     enum GetLockType {
        BLOCK,
        BLOCK_TRY,
        ASYNCH_TRY
    }

    enum FailBehavior {
        NON,
        EXCEPTION
    }
}
