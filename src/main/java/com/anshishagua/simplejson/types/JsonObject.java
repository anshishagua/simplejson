package com.anshishagua.simplejson.types;

import com.anshishagua.simplejson.FormatConfig;
import com.anshishagua.simplejson.JsonConstants;
import com.anshishagua.simplejson.utils.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class JsonObject implements JsonValue {
    private Map<JsonString, JsonValue> map;

    public JsonObject() {
        map = new LinkedHashMap<>();
    }

    public JsonObject(Map<JsonString, JsonValue> map) {
        this.map = new HashMap<>();
        this.map.putAll(map);
    }

    public void put(String key, String value) {
        map.put(new JsonString(key), new JsonString(value));
    }

    public void put(String key, double value) {
        map.put(new JsonString(key), new JsonNumber(value));
    }

    public void put(String key, boolean value) {
        map.put(new JsonString(key), value ? JsonBoolean.TRUE : JsonBoolean.FALSE);
    }

    public void put(String key, JsonValue value) {
        map.put(new JsonString(key), value);
    }

    public void put(JsonString key, JsonValue value) {
        map.put(key, value);
    }

    public String getString(String key) {
        JsonValue jsonValue = map.get(new JsonString(key));

        return ((JsonString) jsonValue).getValue();
    }

    public int getInt(String key) {
        JsonValue jsonValue = map.get(new JsonString(key));

        return ((JsonNumber) jsonValue).getAsInteger();
    }

    public double getDouble(String key) {
        JsonValue jsonValue = map.get(new JsonString(key));

        return ((JsonNumber) jsonValue).getAsDouble();
    }

    public boolean getBoolean(String key) {
        JsonValue jsonValue = map.get(new JsonString(key));

        return ((JsonBoolean) jsonValue).getValue();
    }

    public JsonValue get(JsonString key) {
        return map.get(key);
    }

    public Set<JsonString> keySet() {
        return map.keySet();
    }

    @Override
    public String format(FormatConfig formatConfig) {
        StringBuilder builder = new StringBuilder();

        if (formatConfig.shouldIndent() && formatConfig.shouldIndentStartToken()) {
            builder.append(StringUtils.repeat(formatConfig.getIndentString(), formatConfig.getIndent()));
        }

        builder.append(JsonConstants.LEFT_CURLY_BRACKET);
        builder.append(JsonConstants.NEW_LINE);

        Iterator<Map.Entry<JsonString, JsonValue>> iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<JsonString, JsonValue> entry = iterator.next();

            builder.append(entry.getKey().format(new FormatConfig(formatConfig.shouldIndent(), formatConfig.getIndent() + 1, formatConfig.getIndentString())));

            builder.append(": ");

            if (entry.getValue().isObject()) {
                builder.append(entry.getValue().format(new FormatConfig(formatConfig.shouldIndent(), formatConfig.getIndent() + 1, formatConfig.getIndentString(), false)));
            } else {
                builder.append(entry.getValue().format(new FormatConfig(false, 0)));
            }

            if (iterator.hasNext()) {
                builder.append(JsonConstants.COMMA);
            }

            builder.append(JsonConstants.NEW_LINE);
        }

        if (formatConfig.shouldIndent()) {
            builder.append(StringUtils.repeat(formatConfig.getIndentString(), formatConfig.getIndent()));
        }

        builder.append(JsonConstants.RIGHT_CURLY_BRACKET);

        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(JsonConstants.LEFT_CURLY_BRACKET);

        Iterator<Map.Entry<JsonString, JsonValue>> iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<JsonString, JsonValue> entry = iterator.next();

            builder.append(entry.getKey().toString());

            builder.append(": ");

            builder.append(entry.getValue().toString());

            if (iterator.hasNext()) {
                builder.append(JsonConstants.COMMA);
            }
        }

        builder.append(JsonConstants.RIGHT_CURLY_BRACKET);

        return builder.toString();
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public Object toObject() {
        Map<String, Object> result = new HashMap<>();

        for (JsonString key : map.keySet()) {
            result.put(key.toString(), map.get(key).toObject());
        }

        return result;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.OBJECT;
    }
}
