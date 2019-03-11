package com.anshishagua.simplejson.serialization;

import com.anshishagua.simplejson.JsonException;
import com.anshishagua.simplejson.utils.StringUtils;

import java.lang.reflect.Method;

public class EnumSerializer<T extends Enum> implements JsonSerializer<T> {
    public String serialize(T object) {
        try {
            Method method = object.getClass().getMethod("name");

            method.setAccessible(true);

            return StringUtils.doubleQuote(method.invoke(object));
        } catch (Exception ex) {
            throw new JsonException(ex);
        }
    }

    public T deserialize(String json, Class<T> clazz) {
        try {
            Method method = clazz.getDeclaredMethod("valueOf", String.class);

            method.setAccessible(true);

            if (json.startsWith("\"")) {
                json = json.substring(1, json.length() - 1);
            }

            return (T) method.invoke(null, json);
        } catch (Exception ex) {
            throw new JsonException(ex);
        }
    }
}
