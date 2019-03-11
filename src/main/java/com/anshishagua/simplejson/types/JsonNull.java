package com.anshishagua.simplejson.types;

import com.anshishagua.simplejson.FormatConfig;
import com.anshishagua.simplejson.utils.StringUtils;

public class JsonNull implements JsonValue {
    public static final String NULL_STRING_VALUE = "null";

    public static final JsonNull NULL = new JsonNull();

    private JsonNull() {

    }

    @Override
    public String format(FormatConfig formatConfig) {
        StringBuilder builder = new StringBuilder();

        if (formatConfig.shouldIndent()) {
            builder.append(StringUtils.repeat(formatConfig.getIndentString(), formatConfig.getIndent()));
        }

        builder.append(NULL_STRING_VALUE);

        return builder.toString();
    }

    @Override
    public String toString() {
        return NULL_STRING_VALUE;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public Object toObject() {
        return null;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.NULL;
    }
}
