package com.anshishagua.simplejson.serialization;

import java.math.BigDecimal;
import java.util.Objects;

public class BigDecimalSerializer implements JsonSerializer<BigDecimal> {
    @Override
    public String serialize(BigDecimal object) {
        Objects.requireNonNull(object);

        return object.toString();
    }

    @Override
    public BigDecimal deserialize(String json, Class<BigDecimal> clazz) {
        return new BigDecimal(json);
    }
}
