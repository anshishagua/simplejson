package com.anshishagua.simplejson;

import com.anshishagua.simplejson.annotation.JSONField;
import com.anshishagua.simplejson.serialization.JsonSerializer;
import com.anshishagua.simplejson.serialization.SerializerRegistry;
import com.anshishagua.simplejson.types.JsonArray;
import com.anshishagua.simplejson.types.JsonBoolean;
import com.anshishagua.simplejson.types.JsonNumber;
import com.anshishagua.simplejson.types.JsonObject;
import com.anshishagua.simplejson.types.JsonString;
import com.anshishagua.simplejson.types.JsonValue;
import com.anshishagua.simplejson.types.ValueType;
import com.anshishagua.simplejson.utils.ReflectionUtils;
import com.anshishagua.simplejson.utils.StringUtils;
import com.anshishagua.simplejson.utils.TypeUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
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

public class Json {
    public static JsonValue parse(String json) {
        JsonScanner scanner = new JsonScanner(json);

        JsonValue jsonValue = scanner.parse();

        if (scanner.hasNext()) {
            throw new JsonException("Extra input: " + json.substring(scanner.getIndex()));
        }

        return jsonValue;
    }

    public static JsonObject parseObject(String json) {
        JsonScanner scanner = new JsonScanner(json);

        Object object = scanner.parse();

        if (!(object instanceof JsonObject)) {
            throw new JsonException("Not json object");
        }

        if (scanner.hasNext()) {
            throw new JsonException("Extra input: " + json.substring(scanner.getIndex()));
        }

        return (JsonObject) object;
    }

    public static JsonArray parseArray(String json) {
        JsonScanner scanner = new JsonScanner(json);

        Object object = scanner.parse();

        if (!(object instanceof JsonArray)) {
            throw new JsonException("Not json array");
        }

        if (scanner.hasNext()) {
            throw new JsonException("Extra input: " + json.substring(scanner.getIndex()));
        }

        return (JsonArray) object;
    }

    public static <T> T parse(String json, Class<T> clazz) {
        Objects.requireNonNull(json);

        return parse(Json.parse(json), clazz);
    }

    private static <T> T[] toArray(JsonArray jsonArray, Class<T> clazz) {
        T[] result = (T[]) Array.newInstance(clazz, jsonArray.length());

        for (int i = 0; i < jsonArray.length(); ++i) {
            result[i] = parse(jsonArray.get(i), clazz);
        }

        return result;
    }

    private static <T> T parse(JsonValue jsonValue, Class<T> clazz) {
        if (jsonValue.getValueType() == ValueType.NULL) {
            return null;
        }

        if (clazz == String.class) {
            return (T) ((JsonString) jsonValue).getValue();
        }

        if (clazz == LocalDate.class) {
            JsonString jsonString = (JsonString) jsonValue;

            return SerializerRegistry.get(clazz).deserialize(jsonString.getValue(), clazz);
        }

        if (clazz == LocalDateTime.class) {
            JsonString jsonString = (JsonString) jsonValue;

            return SerializerRegistry.get(clazz).deserialize(jsonString.getValue(), clazz);
        }

        if (clazz == LocalTime.class) {
            JsonString jsonString = (JsonString) jsonValue;

            return SerializerRegistry.get(clazz).deserialize(jsonString.getValue(), clazz);
        }

        if (clazz == Date.class) {
            JsonString jsonString = (JsonString) jsonValue;

            return SerializerRegistry.get(clazz).deserialize(jsonString.getValue(), clazz);
        }

        if (clazz.isEnum()) {
            JsonString jsonString = (JsonString) jsonValue;

            return SerializerRegistry.get(clazz).deserialize(jsonString.getValue(), clazz);
        }

        if (TypeUtils.isPrimitive(clazz)) {
            if (clazz == boolean.class) {
                return (T) (Boolean) ((JsonBoolean) jsonValue).getValue();
            } else if (clazz == byte.class) {
                return (T) (Integer) ((JsonNumber) jsonValue).getAsInteger();
            } else if (clazz == char.class) {
                return (T) (Character) ((JsonString) jsonValue).getValue().charAt(0);
            } else if (clazz == short.class) {
                return (T) (Integer) ((JsonNumber) jsonValue).getAsInteger();
            } else if (clazz == int.class) {
                return (T) (Integer) ((JsonNumber) jsonValue).getAsInteger();
            } else if (clazz == long.class) {
                return (T) (Long) ((JsonNumber) jsonValue).getAsLong();
            } else if (clazz == float.class) {
                return (T) (Float) ((JsonNumber) jsonValue).getAsFloat();
            } else if (clazz == double.class) {
                return (T) (Double) ((JsonNumber) jsonValue).getAsDouble();
            }
        }

        if (TypeUtils.isPrimitiveWrapper(clazz)) {
            if (clazz == Boolean.class) {
                return (T) (Boolean) ((JsonBoolean) jsonValue).getValue();
            } else if (clazz == Byte.class) {
                return (T) (Integer) ((JsonNumber) jsonValue).getAsInteger();
            } else if (clazz == Character.class) {
                return (T) (Character) ((JsonString) jsonValue).getValue().charAt(0);
            } else if (clazz == Short.class) {
                return (T) (Integer) ((JsonNumber) jsonValue).getAsInteger();
            } else if (clazz == Integer.class) {
                return (T) (Integer) ((JsonNumber) jsonValue).getAsInteger();
            } else if (clazz == Long.class) {
                return (T) (Long) ((JsonNumber) jsonValue).getAsLong();
            } else if (clazz == Float.class) {
                return (T) (Float) ((JsonNumber) jsonValue).getAsFloat();
            } else if (clazz == Double.class) {
                return (T) (Double) ((JsonNumber) jsonValue).getAsDouble();
            }
        }

        if (Collection.class.isAssignableFrom(clazz)) {
            Collection result = new ArrayList<>();

            if (Set.class.isAssignableFrom(clazz)) {
                result = new HashSet<>();
            }

            if (jsonValue instanceof JsonObject) {
                throw new JsonException("JsonObject could not be converted to " + clazz.getSimpleName());
            }

            if (jsonValue instanceof JsonArray) {
                JsonArray jsonArray = (JsonArray) jsonValue;

                for (int i = 0; i < jsonArray.length(); ++i) {
                    result.add(jsonArray.get(i).toObject());
                }
            } else {
                result.add(jsonValue.toObject());
            }

            return (T) result;
        }

        if (Map.class.isAssignableFrom(clazz)) {
            if (!(jsonValue instanceof JsonObject)) {
                throw new JsonException("Not json object");
            }

            JsonObject jsonObject = (JsonObject) jsonValue;

            return (T) jsonObject.toObject();
        }

        if (clazz.isArray()) {
            JsonArray jsonArray = (JsonArray) jsonValue;

            if (TypeUtils.isPrimitiveArray(clazz)) {
                Class<?> componentType = clazz.getComponentType();
                if (componentType == boolean.class) {
                    boolean[] result = new boolean[jsonArray.length()];

                    for (int i = 0 ; i < jsonArray.length(); ++i) {
                        JsonBoolean jsonBoolean = (JsonBoolean) jsonArray.get(i);

                        result[i] = jsonBoolean == JsonBoolean.TRUE ? true : false;
                    }

                    return (T) result;
                } else if (componentType == byte.class) {
                    byte[] result = new byte[jsonArray.length()];

                    for (int i = 0 ; i < jsonArray.length(); ++i) {
                        JsonNumber jsonNumber = (JsonNumber) jsonArray.get(i);

                        result[i] = (byte) jsonNumber.getAsInteger();
                    }

                    return (T) result;
                } else if (componentType == char.class) {

                } else if (componentType == short.class) {
                    short[] result = new short[jsonArray.length()];

                    for (int i = 0 ; i < jsonArray.length(); ++i) {
                        JsonNumber jsonNumber = (JsonNumber) jsonArray.get(i);

                        result[i] = (short) jsonNumber.getAsInteger();
                    }

                    return (T) result;
                } else if (componentType == int.class) {
                    int[] result = new int[jsonArray.length()];

                    for (int i = 0 ; i < jsonArray.length(); ++i) {
                        JsonNumber jsonNumber = (JsonNumber) jsonArray.get(i);

                        result[i] = jsonNumber.getAsInteger();
                    }

                    return (T) result;
                } else if (componentType == long.class) {
                    long[] result = new long[jsonArray.length()];

                    for (int i = 0 ; i < jsonArray.length(); ++i) {
                        JsonNumber jsonNumber = (JsonNumber) jsonArray.get(i);

                        result[i] = jsonNumber.getAsLong();
                    }

                    return (T) result;
                } else if (componentType == float.class) {
                    float[] result = new float[jsonArray.length()];

                    for (int i = 0 ; i < jsonArray.length(); ++i) {
                        JsonNumber jsonNumber = (JsonNumber) jsonArray.get(i);

                        result[i] = (float) jsonNumber.getAsDouble();
                    }

                    return (T) result;
                } else if (componentType == double.class) {
                    double[] result = new double[jsonArray.length()];

                    for (int i = 0 ; i < jsonArray.length(); ++i) {
                        JsonNumber jsonNumber = (JsonNumber) jsonArray.get(i);

                        result[i] = jsonNumber.getAsDouble();
                    }

                    return (T) result;
                }
            } else {
                return (T) toArray(jsonArray, clazz.getComponentType());
            }
        }

        JsonObject jsonObject = (JsonObject) jsonValue;

        T object = null;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new JsonException(ex);
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

            JsonValue value = jsonObject.get(new JsonString(fieldName));

            if (value == null) {
                continue;
            }

            try {
                if (value.isObject()) {
                    field.set(object, parse(value, field.getType()));
                } else {
                    if (field.getType().isEnum()) {
                        JsonString jsonString = (JsonString) value;

                        field.set(object, ReflectionUtils.enumValueOf(field.getType(), jsonString.getValue()));
                    } else if (field.getType() == LocalDate.class) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        JsonString jsonString = (JsonString) value;

                        field.set(object, LocalDate.parse(jsonString.getValue(), formatter));
                    } else if (field.getType() == LocalDateTime.class) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        JsonString jsonString = (JsonString) value;

                        field.set(object, LocalDateTime.parse(jsonString.getValue(), formatter));
                    } else if (field.getType() == LocalTime.class) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                        JsonString jsonString = (JsonString) value;

                        field.set(object, LocalTime.parse(jsonString.getValue(), formatter));
                    } else {
                        field.set(object, value.toObject());
                    }
                }
            } catch (Exception ex) {
                throw new JsonException(ex);
            }
        }

        return object;
    }

    public static String toJson(Object object) {
        return toJson(object, new IdentityHashMap<>());
    }

    private static String toJson(Object object, IdentityHashMap<Object, List<Object>> map) {
        if (object == null) {
            return "null";
        }

        if (map.containsKey(object)) {
            throw new JsonException("Circular reference for object " + object);
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
                return ((JsonSerializer<boolean[]>) SerializerRegistry.get(clazz)).serialize((boolean[]) object);
            } else if (clazz == byte[].class) {
                return ((JsonSerializer<byte[]>) SerializerRegistry.get(clazz)).serialize((byte[]) object);
            } else if (clazz == char[].class) {
                return ((JsonSerializer<char[]>) SerializerRegistry.get(clazz)).serialize((char[]) object);
            } else if (clazz == short[].class) {
                return ((JsonSerializer<short[]>) SerializerRegistry.get(clazz)).serialize((short[]) object);
            } else if (clazz == int[].class) {
                return ((JsonSerializer<int[]>) SerializerRegistry.get(clazz)).serialize((int[]) object);
            } else if (clazz == long[].class) {
                return ((JsonSerializer<long[]>) SerializerRegistry.get(clazz)).serialize((long[]) object);
            } else if (clazz == float[].class) {
                return ((JsonSerializer<float[]>) SerializerRegistry.get(clazz)).serialize((float[]) object);
            } else {
                return ((JsonSerializer<double[]>) SerializerRegistry.get(clazz)).serialize((double[]) object);
            }
        }

        if (clazz.isArray()) {
            List<Object> list = new ArrayList<>();

            Object [] array = (Object[]) object;

            for (Object obj : array) {
                list.add(obj);
            }

            return toJson(list, map);
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

        if (object instanceof JsonValue) {
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
            JsonSerializer jsonSerializer = SerializerRegistry.get(object.getClass());

            return jsonSerializer.serialize(object);
        }

        if (!TypeUtils.isUserDefinedObject(clazz)) {
            throw new JsonException("Unsupported type:" + clazz.getName());
        }

        return SerializerRegistry.get(Object.class).serialize(object);
    }

    public static String format(String json) {
        Objects.requireNonNull(json);

        JsonValue jsonValue = parse(json);

        return jsonValue.format(new FormatConfig(true, 0));
    }

    public static String format(JsonValue jsonValue) {
        return jsonValue.format(new FormatConfig(true, 0));
    }

    public static String compress(String json) {
        Objects.requireNonNull(json);

        JsonValue jsonValue = parse(json);

        return compress(jsonValue);
    }

    private static String compress(JsonValue jsonValue) {
        Objects.requireNonNull(jsonValue);

        StringBuilder builder;

        switch (jsonValue.getValueType()) {
            case ARRAY:
                JsonArray jsonArray = (JsonArray) jsonValue;

                builder = new StringBuilder();
                builder.append(JsonConstants.LEFT_BRACKET);

                for (int i = 0; i < jsonArray.length(); ++i) {
                    builder.append(compress(jsonArray.get(i)));

                    if (i != jsonArray.length() - 1) {
                        builder.append(",");
                    }
                }

                builder.append(JsonConstants.RIGHT_BRACKET);

                return builder.toString();
            case OBJECT:
                JsonObject jsonObject = (JsonObject) jsonValue;

                builder = new StringBuilder();
                builder.append(JsonConstants.LEFT_CURLY_BRACKET);

                Iterator<JsonString> iterator = jsonObject.keySet().iterator();

                while (iterator.hasNext()) {
                    JsonString key = iterator.next();

                    builder.append(key.toString());
                    builder.append(":");
                    builder.append(compress(jsonObject.get(key)));

                    if (iterator.hasNext()) {
                        builder.append(JsonConstants.COMMA);
                    }
                }

                builder.append(JsonConstants.RIGHT_CURLY_BRACKET);

                return builder.toString();
            default:
                return jsonValue.toString();
        }
    }

    public static boolean validate(String json) {
        try {
            Json.parse(json);

            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
