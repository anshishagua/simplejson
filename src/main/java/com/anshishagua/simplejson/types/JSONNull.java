package com.anshishagua.simplejson.types;

import com.anshishagua.simplejson.FormatConfig;
import com.anshishagua.simplejson.utils.StringUtils;

public class JSONNull implements JSONValue {
    public static final String NULL_STRING_VALUE = "null";

    public static final JSONNull NULL = new JSONNull();

    private JSONNull() {

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
}
