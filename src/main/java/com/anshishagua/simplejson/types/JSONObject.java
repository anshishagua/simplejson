package com.anshishagua.simplejson.types;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JSONObject implements JSONValue {
    private Map<String, JSONValue> map;

    public JSONObject() {
        map = new HashMap<>();
    }

    public JSONObject(Map<String, JSONValue> map) {
        this.map = new HashMap<>();
        this.map.putAll(map);
    }

    public void put(String key, JSONValue value) {
        map.put(key, value);
    }

    public JSONValue get(String key) {
        return map.get(key);
    }

    @Override
    public String format(int indent) {
        StringBuilder builder = new StringBuilder("{");

        Iterator<Map.Entry<String, JSONValue>> iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, JSONValue> entry = iterator.next();

            builder.append("\"" + entry.getKey() + "\": ");
            builder.append(entry.getValue().format(indent));

            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }

        builder.append("}");

        return builder.toString();
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
