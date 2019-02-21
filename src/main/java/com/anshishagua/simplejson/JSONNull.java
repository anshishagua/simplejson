package com.anshishagua.simplejson;

public class JSONNull {
    public static final JSONNull NULL = new JSONNull();

    private JSONNull() {

    }

    @Override
    public String toString() {
        return "null";
    }
}
