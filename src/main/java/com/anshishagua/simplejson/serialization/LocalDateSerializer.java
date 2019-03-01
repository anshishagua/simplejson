package com.anshishagua.simplejson.serialization;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LocalDateSerializer implements JSONSerializer<LocalDate> {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String serialize(LocalDate value) {
        Objects.requireNonNull(value);

        return FORMATTER.format(value);
    }

    @Override
    public LocalDate deserialize(String json) {
        return LocalDate.parse(json, FORMATTER);
    }
}
