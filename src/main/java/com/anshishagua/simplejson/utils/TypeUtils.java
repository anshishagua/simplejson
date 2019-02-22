package com.anshishagua.simplejson.utils;

public class TypeUtils {
    public static boolean isBasic(Class<?> clazz) {
        return false;
    }

    public static boolean isPrimitive(Class<?> clazz) {
        return clazz == boolean.class || clazz == byte.class ||
                clazz == char.class || clazz == short.class ||
                clazz == int.class || clazz == long.class ||
                clazz == float.class || clazz == double.class;
    }

    public static boolean isPrimitiveWrapper(Class<?> clazz) {
        return clazz == Boolean.class || clazz == Byte.class ||
                clazz == Character.class || clazz == Short.class ||
                clazz == Integer.class || clazz == Long.class ||
                clazz == Float.class || clazz == Double.class;
    }

    public static boolean isPrimitiveArray(Class<?> clazz) {
        return clazz == boolean[].class || clazz == byte[].class ||
                clazz == char[].class || clazz == short[].class ||
                clazz == int[].class || clazz == long[].class ||
                clazz == float[].class || clazz == double[].class;
    }

    public static Object[] primitiveArrayToObjectArray(Object object) {
        return null;
    }
}
