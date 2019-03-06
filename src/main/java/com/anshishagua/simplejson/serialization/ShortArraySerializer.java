package com.anshishagua.simplejson.serialization;

import java.util.Arrays;
import java.util.Objects;

public class ShortArraySerializer implements JSONSerializer<short[]> {
    @Override
    public String serialize(short [] array) {
        Objects.requireNonNull(array);

        StringBuilder builder = new StringBuilder();

        builder.append("[");

        for (int i = 0; i < array.length; ++i) {
            builder.append(array[i]);

            if (i != array.length - 1) {
                builder.append(", ");
            }
        }

        builder.append("]");

        return builder.toString();
    }

    @Override
    public short[] deserialize(String json, Class<short[]> clazz) {
        json = json.substring(1, json.length() - 1);

        if (!json.contains(",")) {
            return new short[0];
        }

        String [] strings = json.split(",");

        short [] array = new short[strings.length];

        for (int i = 0; i < strings.length; ++i) {
            array[i] = Short.parseShort(strings[i].trim());
        }

        return array;
    }
}
