package com.anshishagua.simplejson.types;

import com.anshishagua.simplejson.FormatConfig;

/**
 * JsonValue.java
 *
 * @author lixiao
 * @date 2019-02-21
 */

public interface JsonValue {
    String format(FormatConfig formatConfig);
    boolean isObject();
    Object toObject();
    ValueType getValueType();
}
