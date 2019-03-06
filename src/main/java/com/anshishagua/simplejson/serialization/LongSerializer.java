package com.anshishagua.simplejson.serialization;

import java.util.Objects;

public class LongSerializer implements JSONSerializer<Long> {
    public String serialize(Long value) {
        Objects.requireNonNull(value);

        return value.toString();
    }

    public Long deserialize(String json, Class<Long> clazz) {
        return Long.parseLong(json);
    }
}
