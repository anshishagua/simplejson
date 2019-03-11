package com.anshishagua.simplejson.serialization;

public class FloatSerializer implements JsonSerializer<Float> {
    @Override
    public String serialize(Float object) {
        return object.toString();
    }

    @Override
    public Float deserialize(String json, Class<Float> clazz) {
        return Float.parseFloat(json);
    }
}
