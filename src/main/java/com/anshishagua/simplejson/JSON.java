package com.anshishagua.simplejson;

import com.anshishagua.simplejson.annotation.JSONField;
import com.anshishagua.simplejson.types.JSONArray;
import com.anshishagua.simplejson.types.JSONBoolean;
import com.anshishagua.simplejson.types.JSONNull;
import com.anshishagua.simplejson.types.JSONNumber;
import com.anshishagua.simplejson.types.JSONObject;
import com.anshishagua.simplejson.types.JSONString;
import com.anshishagua.simplejson.types.JSONValue;
import com.anshishagua.simplejson.utils.TypeUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class JSON {
    public static JSONValue parse(String json) {
        JSONScanner scanner = new JSONScanner(json);

        return scanner.parse();
    }

    public static JSONObject parseObject(String json) {
        JSONScanner scanner = new JSONScanner(json);

        Object object = scanner.parse();

        if (!(object instanceof JSONObject)) {
            throw new JSONException("Not json object");
        }

        return (JSONObject) object;
    }

    public static JSONArray parseArray(String json) {
        JSONScanner scanner = new JSONScanner(json);

        Object object = scanner.parse();

        if (!(object instanceof JSONArray)) {
            throw new JSONException("Not json array");
        }

        return (JSONArray) object;
    }

    public static <T> T parse(String json, Class<T> clazz) {
        Objects.requireNonNull(json);

        return parse(JSON.parse(json), clazz);
    }

    private static <T> T[] toArray(JSONArray jsonArray, Class<T> clazz) {
        T[] result = (T[]) Array.newInstance(clazz, jsonArray.length());

        for (int i = 0; i < jsonArray.length(); ++i) {
            result[i] = parse(jsonArray.get(i), clazz);
        }

        return result;
    }

    private static <T> T parse(JSONValue jsonValue, Class<T> clazz) {
        if (jsonValue instanceof JSONNull) {
            return null;
        }

        if (clazz == String.class) {
            return (T) ((JSONString) jsonValue).getValue();
        }

        if (TypeUtils.isPrimitive(clazz)) {
            if (clazz == boolean.class) {
                return (T) (Boolean) ((JSONBoolean) jsonValue).getValue();
            } else if (clazz == byte.class) {
                return (T) (Integer) ((JSONNumber) jsonValue).getAsInteger();
            } else if (clazz == char.class) {
                return (T) (Character) ((JSONString) jsonValue).getValue().charAt(0);
            } else if (clazz == short.class) {
                return (T) (Integer) ((JSONNumber) jsonValue).getAsInteger();
            } else if (clazz == int.class) {
                return (T) (Integer) ((JSONNumber) jsonValue).getAsInteger();
            } else if (clazz == long.class) {
                return (T) (Long) ((JSONNumber) jsonValue).getAsLong();
            } else if (clazz == float.class) {
                return (T) (Float) ((JSONNumber) jsonValue).getAsFloat();
            } else if (clazz == double.class) {
                return (T) (Double) ((JSONNumber) jsonValue).getAsDouble();
            }
        }

        if (TypeUtils.isPrimitiveWrapper(clazz)) {
            if (clazz == Boolean.class) {
                return (T) (Boolean) ((JSONBoolean) jsonValue).getValue();
            } else if (clazz == Byte.class) {
                return (T) (Integer) ((JSONNumber) jsonValue).getAsInteger();
            } else if (clazz == Character.class) {
                return (T) (Character) ((JSONString) jsonValue).getValue().charAt(0);
            } else if (clazz == Short.class) {
                return (T) (Integer) ((JSONNumber) jsonValue).getAsInteger();
            } else if (clazz == Integer.class) {
                return (T) (Integer) ((JSONNumber) jsonValue).getAsInteger();
            } else if (clazz == Long.class) {
                return (T) (Long) ((JSONNumber) jsonValue).getAsLong();
            } else if (clazz == Float.class) {
                return (T) (Float) ((JSONNumber) jsonValue).getAsFloat();
            } else if (clazz == Double.class) {
                return (T) (Double) ((JSONNumber) jsonValue).getAsDouble();
            }
        }

        if (Collection.class.isAssignableFrom(clazz)) {
            Collection result = new ArrayList<>();

            if (Set.class.isAssignableFrom(clazz)) {
                result = new HashSet<>();
            }

            if (jsonValue instanceof JSONObject) {
                throw new JSONException("JSONObject could not be converted to " + clazz.getSimpleName());
            }

            if (jsonValue instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) jsonValue;

                for (int i = 0; i < jsonArray.length(); ++i) {
                    result.add(jsonArray.get(i).toObject());
                }
            } else {
                result.add(jsonValue.toObject());
            }

            return (T) result;
        }

        if (Map.class.isAssignableFrom(clazz)) {
            if (!(jsonValue instanceof JSONObject)) {
                throw new JSONException("Not json object");
            }

            JSONObject jsonObject = (JSONObject) jsonValue;

            return (T) jsonObject.toObject();
        }

        if (clazz.isArray()) {
            JSONArray jsonArray = (JSONArray) jsonValue;

            if (TypeUtils.isPrimitiveArray(clazz)) {
                Class<?> componentType = clazz.getComponentType();
                if (componentType == boolean.class) {
                    boolean[] result = new boolean[jsonArray.length()];

                    for (int i = 0 ; i < jsonArray.length(); ++i) {
                        JSONBoolean jsonBoolean = (JSONBoolean) jsonArray.get(i);

                        result[i] = jsonBoolean == JSONBoolean.TRUE ? true : false;
                    }

                    return (T) result;
                } else if (componentType == byte.class) {
                    byte[] result = new byte[jsonArray.length()];

                    for (int i = 0 ; i < jsonArray.length(); ++i) {
                        JSONNumber jsonNumber = (JSONNumber) jsonArray.get(i);

                        result[i] = (byte) jsonNumber.getAsInteger();
                    }

                    return (T) result;
                } else if (componentType == char.class) {

                } else if (componentType == short.class) {
                    short[] result = new short[jsonArray.length()];

                    for (int i = 0 ; i < jsonArray.length(); ++i) {
                        JSONNumber jsonNumber = (JSONNumber) jsonArray.get(i);

                        result[i] = (short) jsonNumber.getAsInteger();
                    }

                    return (T) result;
                } else if (componentType == int.class) {
                    int[] result = new int[jsonArray.length()];

                    for (int i = 0 ; i < jsonArray.length(); ++i) {
                        JSONNumber jsonNumber = (JSONNumber) jsonArray.get(i);

                        result[i] = jsonNumber.getAsInteger();
                    }

                    return (T) result;
                } else if (componentType == long.class) {
                    long[] result = new long[jsonArray.length()];

                    for (int i = 0 ; i < jsonArray.length(); ++i) {
                        JSONNumber jsonNumber = (JSONNumber) jsonArray.get(i);

                        result[i] = jsonNumber.getAsLong();
                    }

                    return (T) result;
                } else if (componentType == float.class) {
                    float[] result = new float[jsonArray.length()];

                    for (int i = 0 ; i < jsonArray.length(); ++i) {
                        JSONNumber jsonNumber = (JSONNumber) jsonArray.get(i);

                        result[i] = (float) jsonNumber.getAsDouble();
                    }

                    return (T) result;
                } else if (componentType == double.class) {
                    double[] result = new double[jsonArray.length()];

                    for (int i = 0 ; i < jsonArray.length(); ++i) {
                        JSONNumber jsonNumber = (JSONNumber) jsonArray.get(i);

                        result[i] = jsonNumber.getAsDouble();
                    }

                    return (T) result;
                }
            } else {
                return (T) toArray(jsonArray, clazz.getComponentType());

                /*
                Object [] result = new Object[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); ++i) {
                    result[i] = jsonArray.get(i).toObject();
                }

                return (T) result;
                */
            }
        }

        JSONObject jsonObject = (JSONObject) jsonValue;

        T object = null;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new JSONException(ex);
        }

        Field [] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            String fieldName = field.getName();

            if (field.isAnnotationPresent(JSONField.class)) {
                JSONField jsonField = field.getAnnotation(JSONField.class);

                fieldName = jsonField.name();

                if (jsonField.ignore()) {
                    continue;
                }
            }

            JSONValue value = jsonObject.get(new JSONString(fieldName));

            if (value == null) {
                continue;
            }

            try {
                if (value.isObject()) {
                    field.set(object, parse(value, field.getType()));
                } else {
                    field.set(object, value.toObject());
                }
            } catch (Exception ex) {
                throw new JSONException(ex);
            }
        }

        return object;
    }

    public static String toJSONString(Object object) {
        if (object == null) {
            return "null";
        }

        Class<?> clazz = object.getClass();

        if (TypeUtils.isPrimitive(clazz)) {
            if (object.getClass() == char.class) {
                return "\"" + object + "\"";
            } else {
                return object.toString();
            }
        }

        if (TypeUtils.isPrimitiveWrapper(clazz)) {
            if (object.getClass() == Character.class) {
                return "\"" + object + "\"";
            } else {
                return object.toString();
            }
        }

        if (TypeUtils.isPrimitiveArray(clazz)) {
            List<Object> list = new ArrayList<>();

            if (clazz == boolean[].class) {
                for (boolean value : (boolean[]) object) {
                    list.add(value);
                }
            } else if (clazz == byte[].class) {
                for (byte value : (byte[]) object) {
                    list.add(value);
                }
            } else if (clazz == char[].class) {
                for (char value : (char[]) object) {
                    list.add(String.valueOf(value));
                }
            } else if (clazz == short[].class) {
                for (short value : (short[]) object) {
                    list.add(value);
                }
            } else if (clazz == int[].class) {
                for (int value : (int[]) object) {
                    list.add(value);
                }
            } else if (clazz == long[].class) {
                for (long value : (long[]) object) {
                    list.add(value);
                }
            } else if (clazz == float[].class) {
                for (float value : (float[]) object) {
                    list.add(value);
                }
            } else {
                for (double value : (double []) object) {
                    list.add(value);
                }
            }

            return toJSONString(list);
        }

        if (object instanceof JSONValue) {
            return object.toString();
        }

        if (object instanceof Number) {
            return object.toString();
        }

        if (object instanceof String) {
            return "\"" + object + "\"";
        }

        if (object instanceof Map) {
            StringBuilder builder = new StringBuilder("{");

            Iterator<Map.Entry<?, ?>> iterator = ((Map) object).entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<?, ?> entry = iterator.next();

                builder.append(toJSONString(entry.getKey()));
                builder.append(": ");
                builder.append(toJSONString(entry.getValue()));

                if (iterator.hasNext()) {
                    builder.append(", ");
                }
            }

            builder.append("}");

            return builder.toString();
        }

        if (object instanceof Collection) {
            StringBuilder builder = new StringBuilder("[");

            Iterator<Object> iterator = ((Collection) object).iterator();

            while (iterator.hasNext()) {
                builder.append(toJSONString(iterator.next()));

                if (iterator.hasNext()) {
                    builder.append(", ");
                }
            }

            builder.append("]");

            return builder.toString();
        }

        Field [] fields = clazz.getDeclaredFields();

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
            builder.append(toJSONString(value));

            if (i != fields.length - 1) {
                builder.append(", ");
            }
        }

        builder.append("}");

        return builder.toString();
    }

    public static String format(String json) {
        Objects.requireNonNull(json);

        JSONValue jsonValue = parse(json);

        return jsonValue.format(0);
    }

    public static boolean validate(String json) {
        try {
            JSON.parse(json);

            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
