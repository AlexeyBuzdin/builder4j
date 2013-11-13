package com.abuzdin.builder4j;

public class NoFieldFoundException extends RuntimeException {

    public NoFieldFoundException(String fieldName) {
        super("No field found for fieldName: " + fieldName);
    }

    public NoFieldFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
