package com.anshishagua.simplejson.types;

import com.anshishagua.simplejson.utils.StringUtils;

public class JSONBoolean implements JSONValue {
    public static final JSONBoolean TRUE = new JSONBoolean(true);
    public static final JSONBoolean FALSE = new JSONBoolean(false);

    private boolean value;

    private JSONBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public String format(int indent) {
        StringBuilder builder = new StringBuilder();

        builder.append(StringUtils.repeat('\t', indent));
        builder.append(value);

        return builder.toString();
    }


    @Override
    public String toString() {
        return Boolean.toString(value);
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
