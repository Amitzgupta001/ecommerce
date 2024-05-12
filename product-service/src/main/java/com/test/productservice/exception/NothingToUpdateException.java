package com.test.productservice.exception;

public class NothingToUpdateException extends RuntimeException{
    public NothingToUpdateException(String message) {
        super(message);
    }
}
