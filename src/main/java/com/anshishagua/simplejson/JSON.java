package com.anshishagua.simplejson;

import com.anshishagua.simplejson.types.JSONArray;
import com.anshishagua.simplejson.types.JSONObject;
import com.anshishagua.simplejson.types.JSONValue;

import java.util.Objects;

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

    public static String format(String json) {
        Objects.requireNonNull(json);

        JSONValue jsonValue = parse(json);

        return jsonValue.format(0);
    }
}
