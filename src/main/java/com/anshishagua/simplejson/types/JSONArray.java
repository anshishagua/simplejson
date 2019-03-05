package com.anshishagua.simplejson.types;

import com.anshishagua.simplejson.FormatConfig;
import com.anshishagua.simplejson.JSONConstants;
import com.anshishagua.simplejson.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class JSONArray implements JSONValue {
    private List<JSONValue> values;

    public JSONArray() {
        values = new ArrayList<>();
    }

    public JSONArray(JSONValue[] objects) {
        Objects.requireNonNull(objects);

        values = new ArrayList<>(objects.length);

        values.addAll(Arrays.asList(objects));
    }

    public void add(JSONValue value) {
        values.add(value);
    }

    public int length() {
        return values.size();
    }

    public JSONValue get(int index) {
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

        builder.append(JSONConstants.LEFT_BRACKET);
        builder.append(JSONConstants.NEW_LINE);

        Iterator<JSONValue> iterator = values.iterator();

        while (iterator.hasNext()) {
            builder.append(iterator.next().format(new FormatConfig(formatConfig.shouldIndent(), formatConfig.getIndent() + 1, formatConfig.getIndentString())));

            if (iterator.hasNext()) {
                builder.append(JSONConstants.COMMA);
            }

            builder.append(JSONConstants.NEW_LINE);
        }

        if (formatConfig.shouldIndent()) {
            builder.append(StringUtils.repeat(formatConfig.getIndentString(), formatConfig.getIndent()));
        }

        builder.append(JSONConstants.RIGHT_BRACKET);

        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");

        Iterator<JSONValue> iterator = values.iterator();

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

        for (JSONValue jsonValue : values) {
            list.add(jsonValue.toObject());
        }

        return list;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.ARRAY;
    }
}
