package com.anshishagua.simplejson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class JSONArray {
    private List<Object> values;

    public JSONArray() {
        values = new ArrayList<>();
    }

    public JSONArray(Object[] objects) {
        Objects.requireNonNull(objects);

        values = new ArrayList<>(objects.length);

        values.addAll(Arrays.asList(objects));
    }

    public Object get(int index) {
        return values.get(index);
    }

    public String format() {
        return format(0);
    }

    public String format(int indent) {
        StringBuilder builder = new StringBuilder("[");

        builder.append("]");

        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");

        Iterator<Object> iterator = values.iterator();

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
