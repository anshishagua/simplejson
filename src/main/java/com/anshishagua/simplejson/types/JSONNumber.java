package com.anshishagua.simplejson.types;

import java.math.BigInteger;

public class JSONNumber {
    public enum NumberType {
        INTEGER,
        LONG,
        BIGINTEGER,
        DOUBLE,
        BIGDECIMAL
    }

    private NumberType type;
    private Object value;

    public JSONNumber(Object value) {
        this.value = value;

        if (value instanceof Integer) {
            type = NumberType.INTEGER;
        } else if (value instanceof Long) {
            type = NumberType.LONG;
        } else if (value instanceof BigInteger) {
            type = NumberType.BIGINTEGER;
        } else if (value instanceof Double) {
            type = NumberType.DOUBLE;
        } else {
            type = NumberType.BIGDECIMAL;
        }
    }

    public NumberType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
