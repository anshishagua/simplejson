package com.anshishagua.simplejson.serialization;

import com.anshishagua.simplejson.JsonException;
import com.anshishagua.simplejson.utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DateSerializer implements JsonSerializer<Date> {
    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String serialize(Date value) {
        Objects.requireNonNull(value);

        return StringUtils.doubleQuote(FORMAT.format(value));
    }

    @Override
    public Date deserialize(String json, Class<Date> clazz) {
        try {
            if (json.startsWith("\"")) {
                json = json.substring(1, json.length() - 1);
            }

            return FORMAT.parse(json);
        } catch (ParseException ex) {
            throw new JsonException(ex);
        }
    }
}
