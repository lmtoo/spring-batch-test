package cn.lmtoo.batch.retry;

public class CustomRetryException extends Exception {
    public CustomRetryException() {
    }

    public CustomRetryException(String message) {
        super(message);
    }

    public CustomRetryException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomRetryException(Throwable cause) {
        super(cause);
    }

    public CustomRetryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
