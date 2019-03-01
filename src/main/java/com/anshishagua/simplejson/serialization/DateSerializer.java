package com.anshishagua.simplejson.serialization;

import com.anshishagua.simplejson.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DateSerializer implements JSONSerializer<Date> {
    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String serialize(Date value) {
        Objects.requireNonNull(value);

        return FORMAT.format(value);
    }

    @Override
    public Date deserialize(String json) {
        try {
            return FORMAT.parse(json);
        } catch (ParseException ex) {
            throw new JSONException(ex);
        }
    }
}
