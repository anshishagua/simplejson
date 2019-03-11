package com.anshishagua.simplejson.serialization;

public class ShortSerializer implements JsonSerializer<Short> {
    @Override
    public String serialize(Short object) {
        return object.toString();
    }

    @Override
    public Short deserialize(String json, Class<Short> clazz) {
        return Short.parseShort(json);
    }
}
