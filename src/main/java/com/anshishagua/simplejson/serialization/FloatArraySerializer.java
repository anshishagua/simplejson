package com.anshishagua.simplejson.serialization;

import java.util.Arrays;
import java.util.Objects;

public class FloatArraySerializer implements JSONSerializer<float[]> {
    @Override
    public String serialize(float [] array) {
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
    public float[] deserialize(String json, Class<float[]> clazz) {
        json = json.substring(1, json.length() - 1);

        if (!json.contains(",")) {
            return new float[0];
        }

        String [] strings = json.split(",");

        float [] array = new float[strings.length];

        for (int i = 0; i < strings.length; ++i) {
            array[i] = Float.parseFloat(strings[i].trim());
        }

        return array;
    }
}
