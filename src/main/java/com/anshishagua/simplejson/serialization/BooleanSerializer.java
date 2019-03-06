package com.anshishagua.simplejson.serialization;

import java.util.Objects;

public class BooleanSerializer implements JSONSerializer<Boolean> {
    @Override
    public String serialize(Boolean value) {
        Objects.requireNonNull(value);

        return value.toString();
    }

    @Override
    public Boolean deserialize(String json, Class<Boolean> clazz) {
        return Boolean.parseBoolean(json);
    }
}
