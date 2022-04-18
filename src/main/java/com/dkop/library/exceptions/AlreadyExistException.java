package com.dkop.library.exceptions;

public class AlreadyExistException extends Exception {

    public AlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyExistException(String message) {
        super(message);
    }
}
