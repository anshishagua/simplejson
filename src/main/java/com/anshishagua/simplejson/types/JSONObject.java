package com.anshishagua.simplejson.types;

import com.anshishagua.simplejson.FormatConfig;
import com.anshishagua.simplejson.JSONConstants;
import com.anshishagua.simplejson.utils.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class JSONObject implements JSONValue {
    private Map<JSONString, JSONValue> map;

    public JSONObject() {
        map = new LinkedHashMap<>();
    }

    public JSONObject(Map<JSONString, JSONValue> map) {
        this.map = new HashMap<>();
        this.map.putAll(map);
    }

    public void put(String key, String value) {
        map.put(new JSONString(key), new JSONString(value));
    }

    public void put(String key, double value) {
        map.put(new JSONString(key), new JSONNumber(value));
    }

    public void put(String key, boolean value) {
        map.put(new JSONString(key), value ? JSONBoolean.TRUE : JSONBoolean.FALSE);
    }

    public void put(String key, JSONValue value) {
        map.put(new JSONString(key), value);
    }

    public void put(JSONString key, JSONValue value) {
        map.put(key, value);
    }

    public String getString(String key) {
        JSONValue jsonValue = map.get(new JSONString(key));

        return ((JSONString) jsonValue).getValue();
    }

    public int getInt(String key) {
        JSONValue jsonValue = map.get(new JSONString(key));

        return ((JSONNumber) jsonValue).getAsInteger();
    }

    public double getDouble(String key) {
        JSONValue jsonValue = map.get(new JSONString(key));

        return ((JSONNumber) jsonValue).getAsDouble();
    }

    public boolean getBoolean(String key) {
        JSONValue jsonValue = map.get(new JSONString(key));

        return ((JSONBoolean) jsonValue).getValue();
    }

    public JSONValue get(JSONString key) {
        return map.get(key);
    }

    public Set<JSONString> keySet() {
        return map.keySet();
    }

    @Override
    public String format(FormatConfig formatConfig) {
        StringBuilder builder = new StringBuilder();

        if (formatConfig.shouldIndent() && formatConfig.shouldIndentStartToken()) {
            builder.append(StringUtils.repeat(formatConfig.getIndentString(), formatConfig.getIndent()));
        }

        builder.append(JSONConstants.LEFT_CURLY_BRACKET);
        builder.append(JSONConstants.NEW_LINE);

        Iterator<Map.Entry<JSONString, JSONValue>> iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<JSONString, JSONValue> entry = iterator.next();

            builder.append(entry.getKey().format(new FormatConfig(formatConfig.shouldIndent(), formatConfig.getIndent() + 1, formatConfig.getIndentString())));

            builder.append(": ");

            if (entry.getValue().isObject()) {
                builder.append(entry.getValue().format(new FormatConfig(formatConfig.shouldIndent(), formatConfig.getIndent() + 1, formatConfig.getIndentString(), false)));
            } else {
                builder.append(entry.getValue().format(new FormatConfig(false, 0)));
            }

            if (iterator.hasNext()) {
                builder.append(JSONConstants.COMMA);
            }

            builder.append(JSONConstants.NEW_LINE);
        }

        if (formatConfig.shouldIndent()) {
            builder.append(StringUtils.repeat(formatConfig.getIndentString(), formatConfig.getIndent()));
        }

        builder.append(JSONConstants.RIGHT_CURLY_BRACKET);

        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(JSONConstants.LEFT_CURLY_BRACKET);

        Iterator<Map.Entry<JSONString, JSONValue>> iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<JSONString, JSONValue> entry = iterator.next();

            builder.append(entry.getKey().toString());

            builder.append(": ");

            builder.append(entry.getValue().toString());

            if (iterator.hasNext()) {
                builder.append(JSONConstants.COMMA);
            }
        }

        builder.append(JSONConstants.RIGHT_CURLY_BRACKET);

        return builder.toString();
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

    @Override
    public ValueType getValueType() {
        return ValueType.OBJECT;
    }
}
