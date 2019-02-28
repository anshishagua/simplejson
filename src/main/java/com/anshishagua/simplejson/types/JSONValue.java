package com.anshishagua.simplejson.types;

import com.anshishagua.simplejson.FormatConfig;

/**
 * JSONValue.java
 *
 * @author lixiao
 * @date 2019-02-21
 */

public interface JSONValue {
    String format(FormatConfig formatConfig);
    boolean isObject();
    Object toObject();
    ValueType getValueType();
}
