package com.anshishagua.simplejson;

public class JsonException extends RuntimeException {
    public JsonException(Throwable throwable) {
        super(throwable);
    }

    public JsonException() {
        super();
    }

    public JsonException(String message) {
        super(message);
    }
}