package com.anshishagua.simplejson.serialization;

public class ByteSerializer implements JsonSerializer<Byte> {
    @Override
    public String serialize(Byte object) {
        return object.toString();
    }

    @Override
    public Byte deserialize(String json, Class<Byte> clazz) {
        return Byte.parseByte(json);
    }
}
