package com.anshishagua.simplejson.types;

import com.anshishagua.simplejson.utils.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JSONObject implements JSONValue {
    private Map<JSONString, JSONValue> map;

    public JSONObject() {
        map = new HashMap<>();
    }

    public JSONObject(Map<JSONString, JSONValue> map) {
        this.map = new HashMap<>();
        this.map.putAll(map);
    }

    public void put(JSONString key, JSONValue value) {
        map.put(key, value);
    }

    public JSONValue get(JSONString key) {
        return map.get(key);
    }

    @Override
    public String format(int indent) {
        StringBuilder builder = new StringBuilder("{\n");

        Iterator<Map.Entry<JSONString, JSONValue>> iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<JSONString, JSONValue> entry = iterator.next();

            builder.append(entry.getKey().format(indent + 1) + ": ");

            if (entry.getValue().isObject()) {
                builder.append(entry.getValue().format(indent + 1));
            } else {
                builder.append(entry.getValue().format(0));
            }

            if (iterator.hasNext()) {
                builder.append(",");
            }

            builder.append("\n");
        }

        builder.append(StringUtils.repeat('\t', indent));
        builder.append("}");

        return builder.toString();
    }

    @Override
    public String toString() {
        return map.toString();
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public Object toObject() {
        Map<String, Object> result = new HashMap<>();

        for (JSONString key : map.keySet()) {
            result.put(key.toString(), map.get(key).toObject());
        }

        return result;
    }
}
