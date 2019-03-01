package com.anshishagua.simplejson.serialization;

import java.util.Arrays;
import java.util.Objects;

public class LongArraySerializer implements JSONSerializer<long[]> {
    @Override
    public String serialize(long [] array) {
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
    public long[] deserialize(String json) {
        json = json.substring(1, json.length() - 1);

        if (!json.contains(",")) {
            return new long[0];
        }

        String [] strings = json.split(",");

        long [] array = new long[strings.length];

        for (int i = 0; i < strings.length; ++i) {
            array[i] = Long.parseLong(strings[i].trim());
        }

        return array;
    }

    public static void main(String [] args) {
        LongArraySerializer serializer = new LongArraySerializer();

        System.out.println(Arrays.toString(serializer.deserialize("[1,3,5]")));
    }
}
