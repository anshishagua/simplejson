package com.anshishagua.simplejson.utils;

import com.anshishagua.simplejson.JsonException;

import java.lang.reflect.Method;

public class ReflectionUtils {
    public static Object enumValueOf(Class<?> clazz, String name) {
        try {
            Method method = clazz.getDeclaredMethod("valueOf", String.class);

            method.setAccessible(true);
            return method.invoke(null, name);
        } catch (Exception ex) {
            throw new JsonException(ex);
        }
    }
}
