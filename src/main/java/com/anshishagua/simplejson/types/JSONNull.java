package com.anshishagua.simplejson.types;

import com.anshishagua.simplejson.utils.StringUtils;

public class JSONNull implements JSONValue {
    public static final JSONNull NULL = new JSONNull();

    private JSONNull() {

    }

    @Override
    public String format(int indent) {
        StringBuilder builder = new StringBuilder();

        builder.append(StringUtils.repeat('\t', indent));
        builder.append("null");

        return builder.toString();
    }

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public Object toObject() {
        return null;
    }
}
