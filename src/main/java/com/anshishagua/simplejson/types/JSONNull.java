package com.anshishagua.simplejson.types;

public class JSONNull implements JSONValue {
    public static final JSONNull NULL = new JSONNull();

    private JSONNull() {

    }

    @Override
    public String format(int indent) {
        return null;
    }

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public boolean isObject() {
        return false;
    }
}
