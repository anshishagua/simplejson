package com.anshishagua.simplejson.serialization;

import java.io.Serializable;

public interface JsonSerializer<T> extends Serializable {
    String serialize(T object);
    T deserialize(String json, Class<T> clazz);
}
