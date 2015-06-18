package me.buzdin.builder4j;

public class NoFieldFoundException extends RuntimeException {

    public NoFieldFoundException(String fieldName) {
        super("No field and accessors found for fieldName: " + fieldName);
    }

    public NoFieldFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
