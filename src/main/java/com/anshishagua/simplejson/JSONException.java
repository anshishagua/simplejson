package com.anshishagua.simplejson;

public class JSONException extends RuntimeException {
    public JSONException(Throwable throwable) {
        super(throwable);
    }

    public JSONException() {
        super();
    }

    public JSONException(String message) {
        super(message);
    }
}