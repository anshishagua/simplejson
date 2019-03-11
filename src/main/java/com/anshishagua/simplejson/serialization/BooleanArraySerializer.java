package com.anshishagua.simplejson.serialization;

import java.util.Arrays;
import java.util.Objects;

public class BooleanArraySerializer implements JsonSerializer<boolean[]> {
    @Override
    public String serialize(boolean [] array) {
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
    public boolean[] deserialize(String json, Class<boolean[]> clazz) {
        json = json.substring(1, json.length() - 1);

        if (!json.contains(",")) {
            return new boolean[0];
        }

        String [] strings = json.split(",");

        boolean [] array = new boolean [strings.length];

        for (int i = 0; i < strings.length; ++i) {
            array[i] = Boolean.parseBoolean(strings[i].trim());
        }

        return array;
    }

    public static void main(String [] args) {
        BooleanArraySerializer serializer = new BooleanArraySerializer();

        System.out.println(Arrays.toString(serializer.deserialize("[true,false,true]", boolean[].class)));
    }
}
