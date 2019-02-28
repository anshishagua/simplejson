package com.anshishagua.simplejson.types;

import com.anshishagua.simplejson.FormatConfig;
import com.anshishagua.simplejson.utils.StringUtils;

public class JSONBoolean implements JSONValue {
    public static final String TRUE_STRING_VALUE = "true";
    public static final String FALSE_STRING_VALUE = "false";

    public static final JSONBoolean TRUE = new JSONBoolean(true);
    public static final JSONBoolean FALSE = new JSONBoolean(false);

    private boolean value;

    private JSONBoolean(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String format(FormatConfig formatConfig) {
        StringBuilder builder = new StringBuilder();

        if (formatConfig.shouldIndent()) {
            builder.append(StringUtils.repeat(formatConfig.getIndentString(), formatConfig.getIndent()));
        }

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

    @Override
    public ValueType getValueType() {
        return ValueType.BOOLEAN;
    }
}
