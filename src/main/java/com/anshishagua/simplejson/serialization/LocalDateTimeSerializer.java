package com.anshishagua.simplejson.serialization;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LocalDateTimeSerializer implements JSONSerializer<LocalDateTime> {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String serialize(LocalDateTime value) {
        Objects.requireNonNull(value);

        return FORMATTER.format(value);
    }

    @Override
    public LocalDateTime deserialize(String json) {
        return LocalDateTime.parse(json, FORMATTER);
    }
}
