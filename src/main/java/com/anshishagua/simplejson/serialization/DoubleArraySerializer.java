package com.anshishagua.simplejson.serialization;

import java.util.Arrays;
import java.util.Objects;

public class DoubleArraySerializer implements JSONSerializer<double[]> {
    @Override
    public String serialize(double [] array) {
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
    public double[] deserialize(String json, Class<double[]> clazz) {
        json = json.substring(1, json.length() - 1);

        if (!json.contains(",")) {
            return new double[0];
        }

        String [] strings = json.split(",");

        double [] array = new double [strings.length];

        for (int i = 0; i < strings.length; ++i) {
            array[i] = Double.parseDouble(strings[i].trim());
        }

        return array;
    }
}
