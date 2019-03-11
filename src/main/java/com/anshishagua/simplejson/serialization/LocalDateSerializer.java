package com.anshishagua.simplejson.serialization;

import com.anshishagua.simplejson.utils.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LocalDateSerializer implements JsonSerializer<LocalDate> {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String serialize(LocalDate value) {
        Objects.requireNonNull(value);

        return StringUtils.doubleQuote(FORMATTER.format(value));
    }

    @Override
    public LocalDate deserialize(String json, Class<LocalDate> clazz) {
        if (json.startsWith("\"")) {
            json = json.substring(1, json.length() - 1);
        }

        return LocalDate.parse(json, FORMATTER);
    }
}
