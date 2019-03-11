package com.anshishagua.simplejson.types;

import com.anshishagua.simplejson.FormatConfig;
import com.anshishagua.simplejson.JsonConstants;
import com.anshishagua.simplejson.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class JsonArray implements JsonValue {
    private List<JsonValue> values;

    public JsonArray() {
        values = new ArrayList<>();
    }

    public JsonArray(JsonValue[] objects) {
        Objects.requireNonNull(objects);

        values = new ArrayList<>(objects.length);

        values.addAll(Arrays.asList(objects));
    }

    public void put(JsonValue value) {
        values.add(value);
    }

    public void add(JsonValue value) {
        values.add(value);
    }

    public int length() {
        return values.size();
    }

    public JsonValue get(int index) {
        return values.get(index);
    }

    public String format() {
        return format(new FormatConfig(true, 0));
    }

    @Override
    public String format(FormatConfig formatConfig) {
        StringBuilder builder = new StringBuilder();

        if (formatConfig.shouldIndent() && formatConfig.shouldIndentStartToken()) {
            builder.append(StringUtils.repeat(formatConfig.getIndentString(), formatConfig.getIndent()));
        }

        builder.append(JsonConstants.LEFT_BRACKET);
        builder.append(JsonConstants.NEW_LINE);

        Iterator<JsonValue> iterator = values.iterator();

        while (iterator.hasNext()) {
            builder.append(iterator.next().format(new FormatConfig(formatConfig.shouldIndent(), formatConfig.getIndent() + 1, formatConfig.getIndentString())));

            if (iterator.hasNext()) {
                builder.append(JsonConstants.COMMA);
            }

            builder.append(JsonConstants.NEW_LINE);
        }

        if (formatConfig.shouldIndent()) {
            builder.append(StringUtils.repeat(formatConfig.getIndentString(), formatConfig.getIndent()));
        }

        builder.append(JsonConstants.RIGHT_BRACKET);

        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");

        Iterator<JsonValue> iterator = values.iterator();

        while (iterator.hasNext()) {
            builder.append(iterator.next());

            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }

        builder.append("]");

        return builder.toString();
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public Object toObject() {
        List<Object> list = new ArrayList<>(values.size());

        for (JsonValue jsonValue : values) {
            list.add(jsonValue.toObject());
        }

        return list;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.ARRAY;
    }
}
