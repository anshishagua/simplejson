package com.anshishagua.simplejson.types;

import com.anshishagua.simplejson.utils.StringUtils;

import java.util.Objects;

public class JSONString implements JSONValue {
    private final String value;

    public JSONString(String value) {
        Objects.requireNonNull(value);

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JSONString)) {
            return false;
        }

        JSONString jsonString = (JSONString) obj;

        return jsonString.value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String format(int indent) {
        StringBuilder builder = new StringBuilder();
        builder.append(StringUtils.repeat('\t', indent));
        builder.append("\"");
        builder.append(value);
        builder.append("\"");

        return builder.toString();
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public Object toObject() {
        return value;
    }
}
