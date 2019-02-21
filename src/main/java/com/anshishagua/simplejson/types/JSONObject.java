package com.anshishagua.simplejson.types;

import java.util.HashMap;
import java.util.Map;

public class JSONObject {
    private Map<String, Object> map;

    public JSONObject() {
        map = new HashMap<>();
    }

    public JSONObject(Map<String, String> map) {
        this.map = new HashMap<>();
        this.map.putAll(map);
    }

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public Object get(String key) {
        return map.get(key);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
