package com.anshishagua.simplejson.serialization;

import com.anshishagua.simplejson.utils.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LocalDateTimeSerializer implements JSONSerializer<LocalDateTime> {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String serialize(LocalDateTime value) {
        Objects.requireNonNull(value);

        return StringUtils.doubleQuote(FORMATTER.format(value));
    }

    @Override
    public LocalDateTime deserialize(String json, Class<LocalDateTime> clazz) {
        if (json.startsWith("\"")) {
            json = json.substring(1, json.length() - 1);
        }

        return LocalDateTime.parse(json, FORMATTER);
    }
}
