package com.dkop.library.exceptions;

public class UnknownOperationException extends RuntimeException{

    public UnknownOperationException() {
    }

    public UnknownOperationException(String message) {
        super(message);
    }
}
