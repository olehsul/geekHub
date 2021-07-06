package com.owu.geekhub.exception;

public class GenericGeekhubException extends Exception {


    public GenericGeekhubException() {
        super();
    }

    public GenericGeekhubException(String message) {
        super(message);
    }

    public GenericGeekhubException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericGeekhubException(Throwable cause) {
        super(cause);
    }

    protected GenericGeekhubException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }



}
