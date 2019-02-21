package com.anshishagua.simplejson.types;

public class JSONBoolean {
    private boolean value;

    public JSONBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
