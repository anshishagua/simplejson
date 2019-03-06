package com.anshishagua.simplejson.serialization;

import java.math.BigInteger;
import java.util.Objects;

public class BigIntegerSerializer implements JSONSerializer<BigInteger> {
    public String serialize(BigInteger value) {
        Objects.requireNonNull(value);

        return value.toString();
    }

    public BigInteger deserialize(String json, Class<BigInteger> clazz) {
        return new BigInteger(json);
    }
}
