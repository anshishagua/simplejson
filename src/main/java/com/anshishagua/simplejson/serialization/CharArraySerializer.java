package com.anshishagua.simplejson.serialization;

import java.util.Arrays;
import java.util.Objects;

public class CharArraySerializer implements JSONSerializer<char[]> {
    @Override
    public String serialize(char [] array) {
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
    public char[] deserialize(String json, Class<char[]> clazz) {
        json = json.substring(1, json.length() - 1);

        if (!json.contains(",")) {
            return new char[0];
        }

        String [] strings = json.split(",");

        char [] array = new char[strings.length];

        for (int i = 0; i < strings.length; ++i) {
            array[i] = strings[i].charAt(0);
        }

        return array;
    }
}
