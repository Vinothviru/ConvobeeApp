package com.convobee.exception;

public class UserValidationException extends Exception {
    public UserValidationException() {
        super();
    }


    public UserValidationException(String message) {
        super(message);
    }


    public UserValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
