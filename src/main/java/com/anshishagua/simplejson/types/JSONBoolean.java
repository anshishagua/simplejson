package com.anshishagua.simplejson.types;

public class JSONBoolean implements JSONValue {
    public static final JSONBoolean TRUE = new JSONBoolean(true);
    public static final JSONBoolean FALSE = new JSONBoolean(false);

    private boolean value;

    private JSONBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public String format(int indent) {
        return Boolean.toString(value);
    }


    @Override
    public String toString() {
        return Boolean.toString(value);
    }

    @Override
    public boolean isObject() {
        return false;
    }
}
