package com.anshishagua.simplejson.types;

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

    public JSONValue get(int index) {
        return values.get(index);
    }

    public String format() {
        return format(0);
    }

    @Override
    public String format(int indent) {
        StringBuilder builder = new StringBuilder("[");

        Iterator<JSONValue> iterator = values.iterator();

        while (iterator.hasNext()) {
            builder.append(iterator.next().format(indent));

            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }

        builder.append("]");

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
}
