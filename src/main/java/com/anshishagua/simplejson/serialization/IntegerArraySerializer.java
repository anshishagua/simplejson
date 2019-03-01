package com.anshishagua.simplejson.serialization;

import java.util.Arrays;
import java.util.Objects;

public class IntegerArraySerializer implements JSONSerializer<int[]> {
    @Override
    public String serialize(int [] array) {
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
    public int[] deserialize(String json) {
        json = json.substring(1, json.length() - 1);

        if (!json.contains(",")) {
            return new int[0];
        }

        String [] strings = json.split(",");

        int [] array = new int[strings.length];

        for (int i = 0; i < strings.length; ++i) {
            array[i] = Integer.parseInt(strings[i].trim());
        }

        return array;
    }

    public static void main(String [] args) {
        IntegerArraySerializer serializer = new IntegerArraySerializer();

        System.out.println(Arrays.toString(serializer.deserialize("[1,3,5]")));
    }
}
