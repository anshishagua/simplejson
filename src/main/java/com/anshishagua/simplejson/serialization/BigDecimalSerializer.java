package com.anshishagua.simplejson.serialization;

import java.math.BigDecimal;
import java.util.Objects;

public class BigDecimalSerializer implements JSONSerializer<BigDecimal> {
    @Override
    public String serialize(BigDecimal object) {
        Objects.requireNonNull(object);

        return object.toString();
    }

    @Override
    public BigDecimal deserialize(String json) {
        return new BigDecimal(json);
    }
}
