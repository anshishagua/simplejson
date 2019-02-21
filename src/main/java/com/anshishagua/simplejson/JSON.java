package com.anshishagua.simplejson;

import com.anshishagua.simplejson.types.JSONArray;
import com.anshishagua.simplejson.types.JSONObject;

import java.util.Objects;

public class JSON {
    public static Object parse(String json) {
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

    public static String format(String json) {
        Objects.requireNonNull(json);

        Object object = parse(json);

        if (!(object instanceof JSONArray) && !(object instanceof JSONObject)) {
            return object.toString();
        }

        if (object instanceof JSONArray) {

        }

        if (object instanceof JSONObject) {

        }

        if (object instanceof String) {
            return "\"" + object.toString() + "\"";
        }

        return object.toString();
    }
}
