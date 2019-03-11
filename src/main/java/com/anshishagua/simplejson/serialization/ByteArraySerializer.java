package com.anshishagua.simplejson.serialization;

import java.util.Objects;

public class ByteArraySerializer implements JsonSerializer<byte[]> {
    @Override
    public String serialize(byte [] array) {
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
    public byte[] deserialize(String json, Class<byte[]> clazz) {
        json = json.substring(1, json.length() - 1);

        if (!json.contains(",")) {
            return new byte[0];
        }

        String [] strings = json.split(",");

        byte [] array = new byte[strings.length];

        for (int i = 0; i < strings.length; ++i) {
            array[i] = Byte.parseByte(strings[i].trim());
        }

        return array;
    }
}
