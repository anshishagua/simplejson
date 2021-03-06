package com.anshishagua.simplejson.types;

import com.anshishagua.simplejson.FormatConfig;
import com.anshishagua.simplejson.utils.StringUtils;

public class JsonBoolean implements JsonValue {
    public static final String TRUE_STRING_VALUE = "true";
    public static final String FALSE_STRING_VALUE = "false";

    public static final JsonBoolean TRUE = new JsonBoolean(true);
    public static final JsonBoolean FALSE = new JsonBoolean(false);

    private boolean value;

    private JsonBoolean() {

    }

    private JsonBoolean(boolean value) {
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
