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

    boolean useLoginUser() default false;

    /**
     * 1. Constant string key: Don't start with '#'. e.g. mydistributedkey
     * 2. Dynamic object's property key: start with '#'. e.g. #objproperty, #objproperty.prop1.prop2; #arg0, #arg0.prop1
     * Note:
     * If you use parameter name, you must compile your code with "-parameters" on JDK8+
     * Otherwise using 'arg' plus argument index: arg0, arg1, arg2
     */
    String key();

    String prefix() default  "DIS_LOCK_";

    long timeout() default 3000;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * FailBehavior.NON: Never call target method and return null if fail to get a distributed lock.
     * FailBehavior.EXCEPTION: Throw DistributedLockGotFailureException if fail to get a distributed lock.
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
