package com.anshishagua.simplejson.serialization;

import java.util.Collection;
import java.util.Iterator;

public class CollectionSerializer implements JsonSerializer<Collection<?>> {
    @Override
    public String serialize(Collection<?> object) {
        StringBuilder builder = new StringBuilder("[");

        Iterator<?> iterator = object.iterator();

        while (iterator.hasNext()) {
            Object obj = iterator.next();

            if (obj == null) {
                builder.append("null");
            } else {
                JsonSerializer jsonSerializer = SerializerRegistry.get(obj.getClass());

                builder.append(jsonSerializer.serialize(obj));
            }

            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }

        builder.append("]");

        return builder.toString();
    }

    @Override
    public Collection<?> deserialize(String json, Class<Collection<?>> clazz) {
        return null;
    }
}
