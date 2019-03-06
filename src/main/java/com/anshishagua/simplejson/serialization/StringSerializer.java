package com.anshishagua.simplejson.serialization;

import com.anshishagua.simplejson.types.JSONNull;
import com.anshishagua.simplejson.utils.StringUtils;

import java.util.Objects;

public class StringSerializer implements JSONSerializer<String> {
    @Override
    public String serialize(String value) {
        return value == null ? StringUtils.doubleQuote(JSONNull.NULL_STRING_VALUE) : StringUtils.doubleQuote(value);
    }

    @Override
    public String deserialize(String json, Class<String> clazz) {
        Objects.requireNonNull(json);

        return json.substring(1, json.length() - 1);
    }
}
