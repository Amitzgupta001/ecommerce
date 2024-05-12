package com.test.userservice.exception;

public class NothingToUpdateException extends RuntimeException{
    public NothingToUpdateException(String message) {
        super(message);
    }
}
