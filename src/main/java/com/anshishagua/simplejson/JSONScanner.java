package com.anshishagua.simplejson;

import com.anshishagua.simplejson.types.JSONArray;
import com.anshishagua.simplejson.types.JSONBoolean;
import com.anshishagua.simplejson.types.JSONNull;
import com.anshishagua.simplejson.types.JSONNumber;
import com.anshishagua.simplejson.types.JSONObject;
import com.anshishagua.simplejson.types.JSONString;
import com.anshishagua.simplejson.types.JSONValue;
import com.anshishagua.simplejson.utils.StringUtils;

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

    public int getIndex() {
        return index;
    }

    private void skipSpaces() {
        while (index < json.length() && StringUtils.isSpaceChar(json.charAt(index))) {
            ++index;
        }
    }

    private Character getNextToken() {
        while (index < json.length() && StringUtils.isSpaceChar(json.charAt(index))) {
            ++index;
        }

        if (index == json.length()) {
            return null;
        }

        return json.charAt(index);
    }

    public boolean hasNext() {
        return getNextToken() != null;
    }

    private void testHasNext() {
        if (!hasNext()) {
            throw new JSONException("Expect char, but reach to end of json");
        }
    }

    private void testHasNext(String message) {
        if (!hasNext()) {
            throw new JSONException(message);
        }
    }

    public JSONValue parse() {
        testHasNext();

        char ch = getNextToken();

        JSONValue jsonValue = null;

        switch (ch) {
            case JSONConstants.LEFT_CURLY_BRACKET:
                jsonValue = parseJSONObject();
                break;
            case JSONConstants.DOUBLE_QUOTE:
                jsonValue = parseJSONString();
                break;
            case JSONConstants.LEFT_BRACKET:
                jsonValue = parseJSONArray();
                break;
            case 't':
            case 'f':
                jsonValue = parseJSONBoolean();
                break;
            case 'n':
                jsonValue = parseJSONNull();
                break;
            default:
                if (Character.isDigit(ch) || ch == JSONConstants.POSITIVE_SIGN || ch == JSONConstants.NEGATIVE_SIGN) {
                    jsonValue = parseJSONNumber();
                } else {
                    throw new JSONException(String.format("Not valid char %c at pos:%d", ch, index));
                }
        }

        return jsonValue;
    }

    public JSONString parseJSONString() {
        Character ch = null;

        testHasNext();

        if (getNextToken() != JSONConstants.DOUBLE_QUOTE) {
            throw new JSONException(String.format("Invalid start char %c at pos %d for json string",
                    json.charAt(index), index));
        }

        ++index;

        StringBuilder builder = new StringBuilder();

        while (index < json.length()) {
            ch = json.charAt(index);

            switch (ch) {
                case JSONConstants.ESCAPE_CHAR:
                    ++index;

                    testHasNext();

                    char escapeChar = getNextToken();

                    if (StringUtils.isValidEscapeChar(escapeChar)) {
                        builder.append(escapeChar);
                        ++index;
                        break;
                    } else if (escapeChar == 'u') {
                        ++index;

                        if (index + 4 >= json.length()) {
                            throw new JSONException("Expect unicode");
                        }

                        String unicode = json.substring(index, index + 4);

                        if (!StringUtils.isJSONUnicode(unicode)) {
                            throw new JSONException("Not valid unicode:" + unicode);
                        }

                        builder.append((char) Integer.parseInt(unicode, 16));
                        index += 4;
                        break;
                    } else {
                        throw new JSONException("Invalid escape char " + escapeChar + " at pos: " + index);
                    }
                case JSONConstants.DOUBLE_QUOTE:
                    ++index;
                    return new JSONString(builder.toString());
                default:
                    builder.append(ch);
                    ++index;
            }
        }

        throw new JSONException("No end \"");
    }

    public JSONArray parseJSONArray() {
        testHasNext();

        if (getNextToken() != JSONConstants.LEFT_BRACKET) {
            throw new JSONException(String.format("Invalid start char %c at pos %d for json array",
                    json.charAt(index), index));
        }

        ++index;

        List<Object> values = new ArrayList<>();

        Character ch = null;

        while (hasNext()) {
            if (getNextToken() == JSONConstants.RIGHT_BRACKET) {
                ++index;
                break;
            }

            values.add(parse());

            testHasNext();

            ch = getNextToken();

            if (ch == JSONConstants.COMMA) {
                ++index;
            } else if (ch == JSONConstants.RIGHT_BRACKET) {
                ++index;
                break;
            } else {
                throw new JSONException("Invalid char for json array: " + ch);
            }
        }

        JSONArray jsonArray = new JSONArray(values.toArray(new JSONValue[0]));

        return jsonArray;
    }

    public JSONObject parseJSONObject() {
        Character ch = null;

        testHasNext();

        if (getNextToken() != JSONConstants.LEFT_CURLY_BRACKET) {
            throw new JSONException(String.format("Invalid start char %c at pos %d for json object",
                    json.charAt(index), index));
        }

        ++index;

        JSONObject jsonObject = new JSONObject();

        while (hasNext()) {
            if (getNextToken() == JSONConstants.RIGHT_CURLY_BRACKET) {
                ++index;

                return jsonObject;
            }

            JSONString key = parseJSONString();

            testHasNext();

            if (getNextToken() != JSONConstants.KEY_VALUE_SEPARATOR) {
                throw new JSONException(String.format("Invalid char %s at pos %d for key and value sep for json object",
                        json.charAt(index), index));
            }

            ++index;

            JSONValue value = parse();

            jsonObject.put(new JSONString(key.getValue()), value);

            testHasNext("Missing end } for json object");

            ch = getNextToken();

            if (ch == JSONConstants.COMMA) {
                ++index;
            } else if (ch == JSONConstants.RIGHT_CURLY_BRACKET) {
                ++index;

                return jsonObject;
            } else {
                throw new JSONException("Invalid char for json object as sep or end: " + json.charAt(index));
            }
        }

        throw new JSONException("Missing end } for json object");
    }

    public JSONBoolean parseJSONBoolean() {
        testHasNext();

        Character ch = getNextToken();

        if (ch != 't' && ch != 'f') {
            throw new JSONException(String.format("Invalid char %c at pos %d for json boolean",
                    json.charAt(index), index));
        }

        testHasNext();

        ch = getNextToken();

        //true
        if (ch == 't') {
            StringBuilder builder = new StringBuilder();

            while (index < json.length() && Character.isLetter(json.charAt(index))) {
                builder.append(json.charAt(index));
                ++index;
            }

            if (!JSONBoolean.TRUE_STRING_VALUE.equals(builder.toString())) {
                throw new JSONException(String.format("Invalid true value: %s", builder.toString()));
            }

            return JSONBoolean.TRUE;
        }

        StringBuilder builder = new StringBuilder();

        while (index < json.length() && Character.isLetter(json.charAt(index))) {
            builder.append(json.charAt(index));
            ++index;
        }

        if (!JSONBoolean.FALSE_STRING_VALUE.equals(builder.toString())) {
            throw new JSONException(String.format("Invalid false value: %s", builder.toString()));
        }

        return JSONBoolean.FALSE;
    }

    public JSONNull parseJSONNull() {
        testHasNext();

        if (getNextToken() != 'n') {
            throw new JSONException(String.format("Invalid char %c at pos %d for json null",
                    json.charAt(index), index));
        }

        StringBuilder builder = new StringBuilder();

        while (index < json.length() && Character.isLetter(json.charAt(index))) {
            builder.append(json.charAt(index));
            ++index;
        }

        if (!JSONNull.NULL_STRING_VALUE.equals(builder.toString())) {
            throw new JSONException(String.format("Invalid null value:%s", builder.toString()));
        }

        return JSONNull.NULL;
    }

    public JSONNumber parseJSONNumber() {
        StringBuilder builder = new StringBuilder();

        testHasNext("Empty input for json number");

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
                return new JSONNumber(Double.parseDouble(builder.toString()));
            } catch (NumberFormatException ex) {

            }

            return new JSONNumber(new BigDecimal(builder.toString()));
        } else {
            try {
                return new JSONNumber(Integer.parseInt(builder.toString()));
            } catch (NumberFormatException ex) {

            }

            try {
                return new JSONNumber(Long.parseLong(builder.toString()));
            } catch (NumberFormatException ex) {

            }

            return new JSONNumber(new BigInteger(builder.toString()));
        }
    }
}

