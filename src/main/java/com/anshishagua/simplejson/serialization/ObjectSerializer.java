package com.anshishagua.simplejson.serialization;

import com.anshishagua.simplejson.JSON;
import com.anshishagua.simplejson.JSONException;
import com.anshishagua.simplejson.annotation.JSONField;

import java.lang.reflect.Field;

public class ObjectSerializer implements JSONSerializer<Object> {
    public String serialize(Object object) {
        Class clazz = object.getClass();

        Field[] fields = clazz.getDeclaredFields();

        StringBuilder builder = new StringBuilder("{");

        for (int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            field.setAccessible(true);
            Object value = null;

            try {
                value = field.get(object);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new JSONException(ex);
            }

            String fieldName = field.getName();

            if (field.isAnnotationPresent(JSONField.class)) {
                JSONField jsonField = field.getAnnotation(JSONField.class);

                fieldName = jsonField.name();

                if (jsonField.ignore()) {
                    continue;
                }

                if (jsonField.ignoreNull() && value == null) {
                    continue;
                }
            }

            builder.append("\"");
            builder.append(fieldName);
            builder.append("\": ");

            if (value == null) {
                builder.append("null");
            } else {
                JSONSerializer jsonSerializer = SerializerRegistry.get(value.getClass());

                System.out.println(value.getClass());
                builder.append(jsonSerializer.serialize(value));
            }

            if (i != fields.length - 1) {
                builder.append(", ");
            }
        }

        builder.append("}");

        return builder.toString();
    }

    public Object deserialize(String json, Class<Object> clazz) {
        return null;
    }
}
