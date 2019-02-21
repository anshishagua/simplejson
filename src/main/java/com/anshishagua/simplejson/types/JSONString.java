package com.anshishagua.simplejson.types;

public class JSONString implements JSONValue {
    private String value;

    public JSONString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String format(int indent) {
        return String.format("\"%s\"", value);
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
