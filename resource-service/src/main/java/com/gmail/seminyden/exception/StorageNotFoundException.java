package com.gmail.seminyden.exception;

public class StorageNotFoundException extends RuntimeException {

    public StorageNotFoundException(String message) {
        super(message);
    }
}
