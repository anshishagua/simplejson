package com.anshishagua.simplejson.serialization;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SerializerRegistry {
    private SerializerRegistry() {

    }

    private static final Map<Class, JSONSerializer> SERIALIZER_MAP = new HashMap<>();

    static {
        SERIALIZER_MAP.put(boolean.class, new BooleanSerializer());
        SERIALIZER_MAP.put(Boolean.class, new BooleanSerializer());

        SERIALIZER_MAP.put(int.class, new IntegerSerializer());
        SERIALIZER_MAP.put(Integer.class, new IntegerSerializer());

        SERIALIZER_MAP.put(long.class, new LongSerializer());
        SERIALIZER_MAP.put(Long.class, new LongSerializer());

        SERIALIZER_MAP.put(double.class, new DoubleSerializer());
        SERIALIZER_MAP.put(Double.class, new DoubleSerializer());

        SERIALIZER_MAP.put(String.class, new StringSerializer());

        SERIALIZER_MAP.put(Date.class, new DateSerializer());
        SERIALIZER_MAP.put(LocalDate.class, new LocalDateSerializer());
        SERIALIZER_MAP.put(LocalDateTime.class, new LocalDateTimeSerializer());

        SERIALIZER_MAP.put(boolean[].class, new BooleanArraySerializer());
        SERIALIZER_MAP.put(int[].class, new IntegerArraySerializer());
        SERIALIZER_MAP.put(long[].class, new LongArraySerializer());
        SERIALIZER_MAP.put(double[].class, new DoubleArraySerializer());
    }

    public static <T> JSONSerializer<T> get(Class<T> clazz) {
        return SERIALIZER_MAP.get(clazz);
    }
}
