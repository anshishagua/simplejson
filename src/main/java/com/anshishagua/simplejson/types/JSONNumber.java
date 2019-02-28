package com.anshishagua.simplejson.types;

import com.anshishagua.simplejson.FormatConfig;
import com.anshishagua.simplejson.utils.StringUtils;

import java.math.BigDecimal;
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

    public int getAsInteger() {
        return ((Number) value).intValue();
    }

    public long getAsLong() {
        return ((Number) value).longValue();
    }

    public double getAsDouble() {
        return ((Number) value).doubleValue();
    }

    public float getAsFloat() {
        return ((Number) value).floatValue();
    }

    public BigInteger getAsBigInt() {
        return (BigInteger) value;
    }

    public BigDecimal getAsBigDecimal() {
        return (BigDecimal) value;
    }

    @Override
    public String format(FormatConfig formatConfig) {
        StringBuilder builder = new StringBuilder();

        if (formatConfig.shouldIndent()) {
            builder.append(StringUtils.repeat(formatConfig.getIndentString(), formatConfig.getIndent()));
        }

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

    @Override
    public Object toObject() {
        return value;
    }
}
