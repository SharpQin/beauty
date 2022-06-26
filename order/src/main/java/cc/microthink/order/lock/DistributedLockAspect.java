package cc.microthink.order.lock;

import cc.microthink.order.lock.annotation.DistributedKeyLock;
import cc.microthink.order.security.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
public class DistributedLockAspect {

    private Logger log = LoggerFactory.getLogger(DistributedLockAspect.class);

    private final DistributedLocker distributedLocker;

    public DistributedLockAspect(DistributedLocker distributedLocker) {
        this.distributedLocker = distributedLocker;
    }

    private String getKeyName(String key) {
        if (key.indexOf(".") != -1) {
            return key.substring(0, key.indexOf("."));
        }
        else {
            return key;
        }
    }

    private String getKeyValue(String key, Object srcObj) {
        if (key.indexOf(".") == -1) {
            return srcObj.toString();
        }
        String[] properties = key.split("\\.");
        Object targetObj = srcObj;
        for (int i=1; i<properties.length; i++) {
            String property = properties[i];
            String getMethod = "get" + StringUtils.capitalize(property);
            try {
                Method m = targetObj.getClass().getDeclaredMethod(getMethod, null);
                m.setAccessible(true);
                targetObj = m.invoke(targetObj);
            }
            catch (Exception e) {
                log.error("getKeyValue: Fail to getKeyValue.", e);
                throw new RuntimeException("getKeyValue: Fail to getKeyValue.", e);
            }
        }

        return targetObj.toString();
    }

    private String addPrefix(String lockKey, DistributedKeyLock annotation) {
        String prefix = "";
        if (annotation.usePrefix()) {
            prefix = annotation.prefix();
        }
        if (annotation.useLoginUser()) {
            prefix += SecurityUtils.getCurrentUserLogin().orElseThrow();
        }
        if (StringUtils.isNotEmpty(prefix)) {
            log.debug("lockedProcess: Use prefix:{}", prefix);
        }
        return prefix + lockKey;
    }

    @Around(value="@annotation(cc.microthink.order.lock.annotation.DistributedKeyLock)")
    public Object lockedProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = null;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedKeyLock annotation = method.getAnnotation(DistributedKeyLock.class);

        log.debug("lockedProcess: Enter method:{}", method.getName());

        String lockKey;
        //annotation.keyType() == DistributedKeyLock.KeyType.DYNAMIC
        if (annotation.key().startsWith("#")) {
            String key = annotation.key().substring(1);
            String keyName = getKeyName(key);
            Parameter[] parameters = method.getParameters();
            Parameter keyParam = null;
            int index = -1;
            for(int i=0; i<parameters.length; i++) {
                if (parameters[i].getName().equals(keyName)) {
                    keyParam = parameters[i];
                    index = i;
                    break;
                }
            }
            if (keyParam == null) {
                throw new RuntimeException("Invalid DistributedKeyLock configuration");
            }
            Object argValue = joinPoint.getArgs()[index];
            lockKey = getKeyValue(key, argValue);
        }
        else {
            lockKey = annotation.key();
        }
        lockKey = addPrefix(lockKey, annotation);
        if (StringUtils.isBlank(lockKey)) {
            throw new IllegalArgumentException("Invalid distributed key.");
        }

        boolean successLock = false;
        DistributedLock lock = this.distributedLocker.getLock(lockKey);
        try {
            if (annotation.lockType() == DistributedKeyLock.GetLockType.BLOCK) {
                lock.tryLock();
                successLock = true;
            }
            else if(annotation.lockType() == DistributedKeyLock.GetLockType.BLOCK_TRY) {
                successLock = lock.tryLock(annotation.timeout(), annotation.timeUnit());
            }
            else {
                successLock = lock.tryLock();
            }

            if (successLock) {
                proceed = joinPoint.proceed();
            }
            else if (annotation.lockFailBehavior() == DistributedKeyLock.FailBehavior.EXCEPTION) {
                throw new DistributedLockGotFailureException("Fail to get a distributed lock.");
            }
        }
        catch (Exception e) {
            log.error("cancelNotPaymentOrders: Fail to scheduled task for cancelling orders.", e);
            throw e;
        }
        finally {
            if (successLock) {
                lock.unlock();
                log.debug("lockedProcess: unlock method:{}", method.getName());
            }
        }
        return proceed;
    }
}
