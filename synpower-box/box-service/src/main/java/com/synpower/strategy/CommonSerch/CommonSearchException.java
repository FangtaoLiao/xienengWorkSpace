package com.synpower.strategy.CommonSerch;

public class CommonSearchException extends RuntimeException {
    public CommonSearchException() {
        super();
    }

    public CommonSearchException(String message) {
        super(message);
    }

    public CommonSearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonSearchException(Throwable cause) {
        super(cause);
    }

    protected CommonSearchException(String message, Throwable cause,
                                    boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
