package com.anshishagua.simplejson.types;

import com.anshishagua.simplejson.FormatConfig;
import com.anshishagua.simplejson.utils.StringUtils;

import java.util.Objects;

public class JsonString implements JsonValue {
    private final String value;

    public JsonString(String value) {
        Objects.requireNonNull(value);

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JsonString)) {
            return false;
        }

        JsonString jsonString = (JsonString) obj;

        return jsonString.value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String format(FormatConfig formatConfig) {
        StringBuilder builder = new StringBuilder();

        if (formatConfig.shouldIndent()) {
            builder.append(StringUtils.repeat(formatConfig.getIndentString(), formatConfig.getIndent()));
        }

        builder.append(StringUtils.doubleQuote(value));

        return builder.toString();
    }

    @Override
    public String toString() {
        return StringUtils.doubleQuote(value);
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
        return ValueType.STRING;
    }
}
