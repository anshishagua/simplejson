package com.anshishagua.simplejson.serialization;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SerializerRegistry {
    private SerializerRegistry() {

    }

    private static final Map<Class, JsonSerializer> SERIALIZER_MAP = new HashMap<>();

    static {
        SERIALIZER_MAP.put(boolean.class, new BooleanSerializer());
        SERIALIZER_MAP.put(Boolean.class, new BooleanSerializer());

        SERIALIZER_MAP.put(byte.class, new ByteSerializer());
        SERIALIZER_MAP.put(Byte.class, new ByteSerializer());

        SERIALIZER_MAP.put(short.class, new ShortSerializer());
        SERIALIZER_MAP.put(Short.class, new ShortSerializer());

        SERIALIZER_MAP.put(int.class, new IntegerSerializer());
        SERIALIZER_MAP.put(Integer.class, new IntegerSerializer());

        SERIALIZER_MAP.put(long.class, new LongSerializer());
        SERIALIZER_MAP.put(Long.class, new LongSerializer());

        SERIALIZER_MAP.put(float.class, new FloatSerializer());
        SERIALIZER_MAP.put(Float.class, new FloatSerializer());

        SERIALIZER_MAP.put(double.class, new DoubleSerializer());
        SERIALIZER_MAP.put(Double.class, new DoubleSerializer());

        SERIALIZER_MAP.put(String.class, new StringSerializer());

        SERIALIZER_MAP.put(Date.class, new DateSerializer());
        SERIALIZER_MAP.put(LocalDate.class, new LocalDateSerializer());
        SERIALIZER_MAP.put(LocalDateTime.class, new LocalDateTimeSerializer());

        SERIALIZER_MAP.put(boolean[].class, new BooleanArraySerializer());
        SERIALIZER_MAP.put(byte[].class, new ByteArraySerializer());
        SERIALIZER_MAP.put(char[].class, new CharArraySerializer());
        SERIALIZER_MAP.put(short[].class, new ShortArraySerializer());
        SERIALIZER_MAP.put(int[].class, new IntegerArraySerializer());
        SERIALIZER_MAP.put(long[].class, new LongArraySerializer());
        SERIALIZER_MAP.put(float[].class, new FloatArraySerializer());
        SERIALIZER_MAP.put(double[].class, new DoubleArraySerializer());

        SERIALIZER_MAP.put(Enum.class, new EnumSerializer());

        SERIALIZER_MAP.put(Map.class, new MapSerializer());

        SERIALIZER_MAP.put(Collection.class, new CollectionSerializer());

        SERIALIZER_MAP.put(Object.class, new ObjectSerializer());
    }

    public static <T> JsonSerializer<T> get(Class<T> clazz) {
        if (SERIALIZER_MAP.containsKey(clazz)) {
            return SERIALIZER_MAP.get(clazz);
        }

        if (clazz.isEnum()) {
            return SERIALIZER_MAP.get(Enum.class);
        }

        if (Collection.class.isAssignableFrom(clazz)) {
            return SERIALIZER_MAP.get(Collection.class);
        }

        if (Map.class.isAssignableFrom(clazz)) {
            return SERIALIZER_MAP.get(Map.class);
        }

        return SERIALIZER_MAP.get(Object.class);
    }
}
