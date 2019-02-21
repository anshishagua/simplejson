package com.anshishagua.simplejson;

public class JSONString {
    private String value;

    public JSONString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
