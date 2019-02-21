package com.anshishagua.simplejson;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

class JSONScanner {
    private static final String NUMBER_PATTERN_STRING = "-?(0|[1-9][0-9]*)(\\.[0-9]+)?([Ee][+\\-]?(0|[1-9][0-9]*))?";
    private static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER_PATTERN_STRING);

    private String json;
    private int index;

    public JSONScanner(String json) {
        this.json = json;
    }

    private void skipSpaces() {
        while (index < json.length() && Character.isSpaceChar(json.charAt(index))) {
            ++index;
        }
    }

    private boolean hasNext() {
        skipSpaces();

        return index < json.length();
    }

    private boolean isUnicode(String string) {
        for (int i = 0; i < 4; ++i) {
            char ch = string.charAt(i);

            if (ch >= '0' && ch <= '9') {
                continue;
            }

            if (ch >= 'a' && ch <= 'f') {
                continue;
            }

            if (ch >= 'A' && ch <= 'F') {
                continue;
            }

            return false;
        }

        return true;
    }

    public Object parse() {
        skipSpaces();

        if (!hasNext()) {
            return null;
        }

        char ch = json.charAt(index);

        switch (ch) {
            case '{':
                return parseJSONObject();
            case '"':
                return parseJSONString();
            case '[':
                return parseJSONArray();
            case 't':
            case 'f':
                return parseJSONBoolean();
            case 'n':
                return parseJSONNull();
            default:
                if (Character.isDigit(ch) || ch == '-') {
                    return parseJSONNumber();
                }

                throw new JSONException(String.format("Not valid char %c at pos:%d", ch, index));
        }
    }

    public String parseJSONString() {
        if (!hasNext() || json.charAt(index) != '"') {
            throw new JSONException(String.format("Valid start char %c at pos %d for json string",
                    json.charAt(index), index));
        }

        ++index;

        StringBuilder builder = new StringBuilder();

        while (index < json.length()) {
            char ch = json.charAt(index);

            switch (ch) {
                case '\\':
                    ++index;
                    if (index == json.length()) {
                        throw new JSONException("");
                    }

                    char escapeChar = json.charAt(index);
                    if (escapeChar == '"' || escapeChar == '\b' || escapeChar == '\f' || escapeChar == '\t' ||
                            escapeChar == '\r' || escapeChar == '\n' || escapeChar == '\\') {
                        builder.append(escapeChar);
                        ++index;
                    } else if (escapeChar == 'u') {
                        ++index;

                        if (index + 4 >= json.length()) {
                            throw new JSONException("Expect unicode");
                        }

                        String unicode = json.substring(index, index + 4);

                        if (!isUnicode(unicode)) {
                            throw new JSONException("Not valid unicode:" + unicode);
                        }

                        builder.append((char) Integer.parseInt(unicode, 16));
                        index += 4;
                        break;
                    } else {
                        throw new JSONException("Invalid escape char " + ch);
                    }
                case '"':
                    ++index;
                    return builder.toString();
                default:
                    builder.append(ch);
                    ++index;
            }
        }

        throw new JSONException("No end \"");
    }

    public JSONArray parseJSONArray() {
        if (!hasNext() || json.charAt(index) != '[') {
            throw new JSONException(String.format("Invalid start char %c at pos %d for json array",
                    json.charAt(index), index));
        }

        ++index;

        List<Object> values = new ArrayList<>();

        while (hasNext()) {
            skipSpaces();

            char ch = json.charAt(index);

            if (ch == ']') {
                ++index;
                break;
            }

            values.add(parse());

            skipSpaces();

            if (ch == ',') {
                ++index;
            } else if (ch == ']') {
                ++index;
                break;
            } else {
                throw new JSONException("Invalid char for json array: " + ch);
            }
        }

        JSONArray jsonArray = new JSONArray(values.toArray(new Object[0]));

        return jsonArray;
    }

    public JSONObject parseJSONObject() {
        if (!hasNext() || json.charAt(index) != '{') {
            throw new JSONException(String.format("Invalid start char %c at pos %d for json object",
                    json.charAt(index), index));
        }

        ++index;

        JSONObject jsonObject = new JSONObject();

        while (hasNext()) {
            skipSpaces();

            if (json.charAt(index) == '}') {
                ++index;

                return jsonObject;
            }

            String key = parseJSONString();

            skipSpaces();

            if (index == json.length() || json.charAt(index) != ':') {
                throw new JSONException(String.format("Invalid char %s at pos %d for key and value sep for json object",
                        json.charAt(index), index));
            }

            ++index;

            Object value = parse();

            jsonObject.put(key, value);

            skipSpaces();

            if (index == json.length()) {
                throw new JSONException("Missing end } for json object");
            }

            if (json.charAt(index) == ',') {
                ++index;
            } else if (json.charAt(index) == '}') {
                ++index;

                return jsonObject;
            } else {
                throw new JSONException("Invalid char for json object as sep or end: " + json.charAt(index));
            }
        }

        throw new JSONException("Missing end } for json object");
    }

    public Boolean parseJSONBoolean() {
        if (!hasNext() || (json.charAt(index) != 't' && json.charAt(index) != 'f')) {
            throw new JSONException(String.format("Invalid char %c at pos %d for json boolean",
                    json.charAt(index), index));
        }

        char ch = json.charAt(index);

        //true
        if (ch == 't') {
            StringBuilder builder = new StringBuilder();

            while (index < json.length() && Character.isLetter(json.charAt(index))) {
                builder.append(json.charAt(index));
                ++index;
            }

            if (!"true".equals(builder.toString())) {
                throw new JSONException(String.format("Invalid true value: %s", builder.toString()));
            }

            return true;
        }

        StringBuilder builder = new StringBuilder();

        while (index < json.length() && Character.isLetter(json.charAt(index))) {
            builder.append(json.charAt(index));
            ++index;
        }

        if (!"false".equals(builder.toString())) {
            throw new JSONException(String.format("Invalid false value: %s", builder.toString()));
        }

        return false;
    }

    public JSONNull parseJSONNull() {
        if (!hasNext() || (json.charAt(index) != 'n')) {
            throw new JSONException(String.format("Invalid char %c at pos %d for json null",
                    json.charAt(index), index));
        }

        StringBuilder builder = new StringBuilder();

        while (index < json.length() && Character.isLetter(json.charAt(index))) {
            builder.append(json.charAt(index));
            ++index;
        }

        if (!"null".equals(builder.toString())) {
            throw new JSONException(String.format("Invalid null value:%s", builder.toString()));
        }

        return JSONNull.NULL;
    }

    public Object parseJSONNumber() {
        StringBuilder builder = new StringBuilder();

        if (index == json.length()) {
            throw new JSONException("Empty input for json number");
        }

        while (index < json.length()) {
            char ch = json.charAt(index);

            if (Character.isDigit(ch)) {
                builder.append(ch);
                ++index;
            } else if (ch == '.' || ch == '-' || ch == 'E' || ch == 'e') {
                builder.append(ch);
                ++index;
            } else {
                break;
            }
        }

        if (builder.indexOf(".") >= 0) {
            try {
                return Double.parseDouble(builder.toString());
            } catch (NumberFormatException ex) {

            }

            return new BigDecimal(builder.toString());
        } else {
            try {
                return Integer.parseInt(builder.toString());
            } catch (NumberFormatException ex) {

            }

            try {
                return Long.parseLong(builder.toString());
            } catch (NumberFormatException ex) {

            }

            return new BigInteger(builder.toString());
        }
    }
}

