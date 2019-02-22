package com.anshishagua.simplejson.types;

import com.anshishagua.simplejson.utils.StringUtils;

import java.math.BigInteger;

public class JSONNumber implements JSONValue {
    public enum NumberType {
        INTEGER,
        LONG,
        BIGINTEGER,
        DOUBLE,
        BIGDECIMAL
    }

    private NumberType numberType;
    private Object value;

    public JSONNumber(Object value) {
        this.value = value;

        if (value instanceof Integer) {
            numberType = NumberType.INTEGER;
        } else if (value instanceof Long) {
            numberType = NumberType.LONG;
        } else if (value instanceof BigInteger) {
            numberType = NumberType.BIGINTEGER;
        } else if (value instanceof Double) {
            numberType = NumberType.DOUBLE;
        } else {
            numberType = NumberType.BIGDECIMAL;
        }
    }

    public NumberType getNumberType() {
        return numberType;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String format(int indent) {
        StringBuilder builder = new StringBuilder();
        builder.append(StringUtils.repeat('\t', indent));

        builder.append(value);

        return builder.toString();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean isObject() {
        return false;
    }
}
