package com.anshishagua.simplejson.serialization;

import java.util.Objects;

public class DoubleSerializer implements JSONSerializer<Double> {
    public String serialize(Double value) {
        Objects.requireNonNull(value);

        return value.toString();
    }

    public Double deserialize(String json) {
        return Double.parseDouble(json);
    }
}
