package com.anshishagua.simplejson.serialization;

public class IntegerSerializer implements JSONSerializer<Integer> {
    public String serialize(Integer object) {
        return object.toString();
    }

    public Integer deserialize(String json, Class<Integer> clazz) {
        return Integer.parseInt(json);
    }
}
