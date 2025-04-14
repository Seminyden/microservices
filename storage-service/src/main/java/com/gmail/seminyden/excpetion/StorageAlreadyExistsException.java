package com.gmail.seminyden.excpetion;

public class StorageAlreadyExistsException extends RuntimeException {
    public StorageAlreadyExistsException(String message) {
        super(message);
    }
}
