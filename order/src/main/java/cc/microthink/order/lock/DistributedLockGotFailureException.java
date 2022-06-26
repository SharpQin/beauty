package cc.microthink.order.lock;

public class DistributedLockGotFailureException extends RuntimeException {

    public DistributedLockGotFailureException(String message) {
        super(message);
    }

    public DistributedLockGotFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public DistributedLockGotFailureException(Throwable cause) {
        super(cause);
    }
}
