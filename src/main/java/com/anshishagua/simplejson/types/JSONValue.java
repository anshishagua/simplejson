package com.anshishagua.simplejson.types;

/**
 * JSONValue.java
 *
 * @author lixiao
 * @date 2019-02-21
 */

public interface JSONValue {
    String format(int indent);
    boolean isObject();
}
