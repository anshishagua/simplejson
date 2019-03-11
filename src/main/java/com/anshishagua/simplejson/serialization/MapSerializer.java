package com.anshishagua.simplejson.serialization;

import java.util.Iterator;
import java.util.Map;

public class MapSerializer implements JsonSerializer<Map<?, ?>> {
    public String serialize(Map<?, ?> object) {
        StringBuilder builder = new StringBuilder("{");

        Iterator<Map.Entry<?, ?>> iterator = ((Map) object).entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<?, ?> entry = iterator.next();

            JsonSerializer jsonSerializer = SerializerRegistry.get(entry.getKey().getClass());

            builder.append(jsonSerializer.serialize(entry.getKey()));
            builder.append(": ");

            Object value = entry.getValue();
            if (value == null) {
                builder.append("null");
            } else {
                jsonSerializer = SerializerRegistry.get(entry.getValue().getClass());

                builder.append(jsonSerializer.serialize(value));
            }

            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }

        builder.append("}");

        return builder.toString();
    }

    public Map<?, ?> deserialize(String json, Class<Map<?, ?>> clazz) {
        return null;
    }
}
