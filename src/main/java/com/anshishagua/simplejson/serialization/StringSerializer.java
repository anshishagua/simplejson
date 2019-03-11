package com.anshishagua.simplejson.serialization;

import com.anshishagua.simplejson.types.JsonNull;
import com.anshishagua.simplejson.utils.StringUtils;

import java.util.Objects;

public class StringSerializer implements JsonSerializer<String> {
    @Override
    public String serialize(String value) {
        return value == null ? StringUtils.doubleQuote(JsonNull.NULL_STRING_VALUE) : StringUtils.doubleQuote(value);
    }

    @Override
    public String deserialize(String json, Class<String> clazz) {
        Objects.requireNonNull(json);

        return json.substring(1, json.length() - 1);
    }
}
