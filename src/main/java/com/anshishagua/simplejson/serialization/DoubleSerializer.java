package com.anshishagua.simplejson.serialization;

import java.util.Objects;

public class DoubleSerializer implements JsonSerializer<Double> {
    public String serialize(Double value) {
        Objects.requireNonNull(value);

        return value.toString();
    }

    public Double deserialize(String json, Class<Double> clazz) {
        return Double.parseDouble(json);
    }
}
