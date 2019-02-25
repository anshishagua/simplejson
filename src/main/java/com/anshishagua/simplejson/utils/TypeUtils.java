package com.anshishagua.simplejson.utils;

import java.util.HashSet;
import java.util.Set;

public class TypeUtils {
    public static final Set<String> PRIMITIVE_TYPE_NAMES = null;

    private Set<String> getPrimitiveTypeNames() {
        Set<String> set = new HashSet<>();

        set.add(boolean.class.getSimpleName());
        set.add(byte.class.getSimpleName());
        set.add(char.class.getSimpleName());
        set.add(short.class.getSimpleName());
        set.add(int.class.getSimpleName());
        set.add(long.class.getSimpleName());
        set.add(float.class.getSimpleName());
        set.add(double.class.getSimpleName());

        return set;
    }

    public static boolean isBasic(Class<?> clazz) {
        return false;
    }

    public static boolean isPrimitive(String typeName) {
        return PRIMITIVE_TYPE_NAMES.contains(typeName);
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
