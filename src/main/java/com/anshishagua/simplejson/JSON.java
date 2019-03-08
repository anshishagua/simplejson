package com.anshishagua.simplejson;

import com.anshishagua.simplejson.annotation.JSONField;
import com.anshishagua.simplejson.serialization.BooleanArraySerializer;
import com.anshishagua.simplejson.serialization.ByteArraySerializer;
import com.anshishagua.simplejson.serialization.DoubleArraySerializer;
import com.anshishagua.simplejson.serialization.FloatArraySerializer;
import com.anshishagua.simplejson.serialization.IntegerArraySerializer;
import com.anshishagua.simplejson.serialization.JSONSerializer;
import com.anshishagua.simplejson.serialization.LongArraySerializer;
import com.anshishagua.simplejson.serialization.SerializerRegistry;
import com.anshishagua.simplejson.serialization.ShortArraySerializer;
import com.anshishagua.simplejson.types.JSONArray;
import com.anshishagua.simplejson.types.JSONBoolean;
import com.anshishagua.simplejson.types.JSONNull;
import com.anshishagua.simplejson.types.JSONNumber;
import com.anshishagua.simplejson.types.JSONObject;
import com.anshishagua.simplejson.types.JSONString;
import com.anshishagua.simplejson.types.JSONValue;
import com.anshishagua.simplejson.types.ValueType;
import com.anshishagua.simplejson.utils.ReflectionUtils;
import com.anshishagua.simplejson.utils.StringUtils;
import com.anshishagua.simplejson.utils.TypeUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class JSON {
    public static JSONValue parse(String json) {
        JSONScanner scanner = new JSONScanner(json);

        JSONValue jsonValue = scanner.parse();

        if (scanner.hasNext()) {
            throw new JSONException("Extra input: " + json.substring(scanner.getIndex()));
        }

        return jsonValue;
    }

    public static JSONObject parseObject(String json) {
        JSONScanner scanner = new JSONScanner(json);

        Object object = scanner.parse();

        if (!(object instanceof JSONObject)) {
            throw new JSONException("Not json object");
        }

        if (scanner.hasNext()) {
            throw new JSONException("Extra input: " + json.substring(scanner.getIndex()));
        }

        return (JSONObject) object;
    }

    public static JSONArray parseArray(String json) {
        JSONScanner scanner = new JSONScanner(json);

        Object object = scanner.parse();

        if (!(object instanceof JSONArray)) {
            throw new JSONException("Not json array");
        }

        if (scanner.hasNext()) {
            throw new JSONException("Extra input: " + json.substring(scanner.getIndex()));
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
        if (jsonValue.getValueType() == ValueType.NULL) {
            return null;
        }

        if (clazz == String.class) {
            return (T) ((JSONString) jsonValue).getValue();
        }

        if (clazz == LocalDate.class) {
            JSONString jsonString = (JSONString) jsonValue;

            return SerializerRegistry.get(clazz).deserialize(jsonString.getValue(), clazz);
        }

        if (clazz == LocalDateTime.class) {
            JSONString jsonString = (JSONString) jsonValue;

            return SerializerRegistry.get(clazz).deserialize(jsonString.getValue(), clazz);
        }

        if (clazz == LocalTime.class) {
            JSONString jsonString = (JSONString) jsonValue;

            return SerializerRegistry.get(clazz).deserialize(jsonString.getValue(), clazz);
        }

        if (clazz == Date.class) {
            JSONString jsonString = (JSONString) jsonValue;

            return SerializerRegistry.get(clazz).deserialize(jsonString.getValue(), clazz);
        }

        if (clazz.isEnum()) {
            JSONString jsonString = (JSONString) jsonValue;

            return SerializerRegistry.get(clazz).deserialize(jsonString.getValue(), clazz);
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
                    if (field.getType().isEnum()) {
                        JSONString jsonString = (JSONString) value;

                        field.set(object, ReflectionUtils.enumValueOf(field.getType(), jsonString.getValue()));
                    } else if (field.getType() == LocalDate.class) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        JSONString jsonString = (JSONString) value;

                        field.set(object, LocalDate.parse(jsonString.getValue(), formatter));
                    } else if (field.getType() == LocalDateTime.class) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        JSONString jsonString = (JSONString) value;

                        field.set(object, LocalDateTime.parse(jsonString.getValue(), formatter));
                    } else if (field.getType() == LocalTime.class) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                        JSONString jsonString = (JSONString) value;

                        field.set(object, LocalTime.parse(jsonString.getValue(), formatter));
                    } else {
                        field.set(object, value.toObject());
                    }
                }
            } catch (Exception ex) {
                throw new JSONException(ex);
            }
        }

        return object;
    }

    public static String toJSONString(Object object) {
        return toJSONString(object, new IdentityHashMap<>());
    }

    private static String toJSONString(Object object, IdentityHashMap<Object, List<Object>> map) {
        if (object == null) {
            return "null";
        }

        if (map.containsKey(object)) {
            throw new JSONException("Circular reference for object " + object);
        }

        Class<?> clazz = object.getClass();

        if (TypeUtils.isPrimitive(clazz)) {
            if (object.getClass() == char.class) {
                return StringUtils.doubleQuote(object);
            } else {
                return object.toString();
            }
        }

        if (TypeUtils.isPrimitiveWrapper(clazz)) {
            if (object.getClass() == Character.class) {
                return StringUtils.doubleQuote(object);
            } else {
                return object.toString();
            }
        }

        if (TypeUtils.isPrimitiveArray(clazz)) {
            if (clazz == boolean[].class) {
                return ((JSONSerializer<boolean[]>) SerializerRegistry.get(clazz)).serialize((boolean[]) object);
            } else if (clazz == byte[].class) {
                return ((JSONSerializer<byte[]>) SerializerRegistry.get(clazz)).serialize((byte[]) object);
            } else if (clazz == char[].class) {
                return ((JSONSerializer<char[]>) SerializerRegistry.get(clazz)).serialize((char[]) object);
            } else if (clazz == short[].class) {
                return ((JSONSerializer<short[]>) SerializerRegistry.get(clazz)).serialize((short[]) object);
            } else if (clazz == int[].class) {
                return ((JSONSerializer<int[]>) SerializerRegistry.get(clazz)).serialize((int[]) object);
            } else if (clazz == long[].class) {
                return ((JSONSerializer<long[]>) SerializerRegistry.get(clazz)).serialize((long[]) object);
            } else if (clazz == float[].class) {
                return ((JSONSerializer<float[]>) SerializerRegistry.get(clazz)).serialize((float[]) object);
            } else {
                return ((JSONSerializer<double[]>) SerializerRegistry.get(clazz)).serialize((double[]) object);
            }
        }

        if (clazz.isArray()) {
            List<Object> list = new ArrayList<>();

            Object [] array = (Object[]) object;

            for (Object obj : array) {
                list.add(obj);
            }

            return toJSONString(list, map);
        }

        if (clazz.isEnum()) {
            return SerializerRegistry.get(Enum.class).serialize((Enum) object);
        }

        if (clazz == LocalDate.class) {
            return SerializerRegistry.get(LocalDate.class).serialize((LocalDate) object);
        }

        if (clazz == LocalDateTime.class) {
            return SerializerRegistry.get(LocalDateTime.class).serialize((LocalDateTime) object);
        }

        if (object instanceof JSONValue) {
            return object.toString();
        }

        if (object instanceof Number) {
            return object.toString();
        }

        if (object instanceof String) {
            return StringUtils.doubleQuote(object);
        }

        if (object instanceof Map) {
            return SerializerRegistry.get(Map.class).serialize((Map<?,?>) object);
        }

        if (object instanceof Collection) {
            JSONSerializer jsonSerializer = SerializerRegistry.get(object.getClass());

            return jsonSerializer.serialize(object);
        }

        if (!TypeUtils.isUserDefinedObject(clazz)) {
            throw new JSONException("Unsupported type:" + clazz.getName());
        }

        return SerializerRegistry.get(Object.class).serialize(object);
    }

    public static String format(String json) {
        Objects.requireNonNull(json);

        JSONValue jsonValue = parse(json);

        return jsonValue.format(new FormatConfig(true, 0));
    }

    public static String format(JSONValue jsonValue) {
        return jsonValue.format(new FormatConfig(true, 0));
    }

    public static String compress(String json) {
        Objects.requireNonNull(json);

        JSONValue jsonValue = parse(json);

        return compress(jsonValue);
    }

    private static String compress(JSONValue jsonValue) {
        Objects.requireNonNull(jsonValue);

        StringBuilder builder;

        switch (jsonValue.getValueType()) {
            case ARRAY:
                JSONArray jsonArray = (JSONArray) jsonValue;

                builder = new StringBuilder();
                builder.append(JSONConstants.LEFT_BRACKET);

                for (int i = 0; i < jsonArray.length(); ++i) {
                    builder.append(compress(jsonArray.get(i)));

                    if (i != jsonArray.length() - 1) {
                        builder.append(",");
                    }
                }

                builder.append(JSONConstants.RIGHT_BRACKET);

                return builder.toString();
            case OBJECT:
                JSONObject jsonObject = (JSONObject) jsonValue;

                builder = new StringBuilder();
                builder.append(JSONConstants.LEFT_CURLY_BRACKET);

                Iterator<JSONString> iterator = jsonObject.keySet().iterator();

                while (iterator.hasNext()) {
                    JSONString key = iterator.next();

                    builder.append(key.toString());
                    builder.append(":");
                    builder.append(compress(jsonObject.get(key)));

                    if (iterator.hasNext()) {
                        builder.append(JSONConstants.COMMA);
                    }
                }

                builder.append(JSONConstants.RIGHT_CURLY_BRACKET);

                return builder.toString();
            default:
                return jsonValue.toString();
        }
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
