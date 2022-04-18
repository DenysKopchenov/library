package com.dkop.library.exceptions;

public class UnableToDeleteException extends Exception {

    public UnableToDeleteException(String message) {
        super(message);
    }

    public UnableToDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
