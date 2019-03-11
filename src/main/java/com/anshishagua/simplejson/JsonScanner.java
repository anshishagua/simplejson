package com.anshishagua.simplejson;

import com.anshishagua.simplejson.types.JsonArray;
import com.anshishagua.simplejson.types.JsonBoolean;
import com.anshishagua.simplejson.types.JsonNull;
import com.anshishagua.simplejson.types.JsonNumber;
import com.anshishagua.simplejson.types.JsonObject;
import com.anshishagua.simplejson.types.JsonString;
import com.anshishagua.simplejson.types.JsonValue;
import com.anshishagua.simplejson.utils.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

class JsonScanner {
    private static final String NUMBER_PATTERN_STRING = "-?(0|[1-9][0-9]*)(\\.[0-9]+)?([Ee][+\\-]?(0|[1-9][0-9]*))?";
    private static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER_PATTERN_STRING);

    private String json;
    private int index;

    public JsonScanner(String json) {
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
            throw new JsonException("Expect char, but reach to end of json");
        }
    }

    private void testHasNext(String message) {
        if (!hasNext()) {
            throw new JsonException(message);
        }
    }

    public JsonValue parse() {
        testHasNext();

        char ch = getNextToken();

        JsonValue jsonValue = null;

        switch (ch) {
            case JsonConstants.LEFT_CURLY_BRACKET:
                jsonValue = parseJsonObject();
                break;
            case JsonConstants.DOUBLE_QUOTE:
                jsonValue = parseJsonString();
                break;
            case JsonConstants.LEFT_BRACKET:
                jsonValue = parseJsonArray();
                break;
            case 't':
            case 'f':
                jsonValue = parseJsonBoolean();
                break;
            case 'n':
                jsonValue = parseJsonNull();
                break;
            default:
                if (Character.isDigit(ch) || ch == JsonConstants.POSITIVE_SIGN || ch == JsonConstants.NEGATIVE_SIGN) {
                    jsonValue = parseJsonNumber();
                } else {
                    throw new JsonException(String.format("Not valid char %c at pos:%d", ch, index));
                }
        }

        return jsonValue;
    }

    public JsonString parseJsonString() {
        Character ch = null;

        testHasNext();

        if (getNextToken() != JsonConstants.DOUBLE_QUOTE) {
            throw new JsonException(String.format("Invalid start char %c at pos %d for json string",
                    json.charAt(index), index));
        }

        ++index;

        StringBuilder builder = new StringBuilder();

        while (index < json.length()) {
            ch = json.charAt(index);

            switch (ch) {
                case JsonConstants.ESCAPE_CHAR:
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
                            throw new JsonException("Expect unicode");
                        }

                        String unicode = json.substring(index, index + 4);

                        if (!StringUtils.isJSONUnicode(unicode)) {
                            throw new JsonException("Not valid unicode:" + unicode);
                        }

                        builder.append((char) Integer.parseInt(unicode, 16));
                        index += 4;
                        break;
                    } else {
                        throw new JsonException("Invalid escape char " + escapeChar + " at pos: " + index);
                    }
                case JsonConstants.DOUBLE_QUOTE:
                    ++index;
                    return new JsonString(builder.toString());
                default:
                    builder.append(ch);
                    ++index;
            }
        }

        throw new JsonException("No end \"");
    }

    public JsonArray parseJsonArray() {
        testHasNext();

        if (getNextToken() != JsonConstants.LEFT_BRACKET) {
            throw new JsonException(String.format("Invalid start char %c at pos %d for json array",
                    json.charAt(index), index));
        }

        ++index;

        List<Object> values = new ArrayList<>();

        Character ch = null;

        while (hasNext()) {
            if (getNextToken() == JsonConstants.RIGHT_BRACKET) {
                ++index;
                break;
            }

            values.add(parse());

            testHasNext();

            ch = getNextToken();

            if (ch == JsonConstants.COMMA) {
                ++index;
            } else if (ch == JsonConstants.RIGHT_BRACKET) {
                ++index;
                break;
            } else {
                throw new JsonException("Invalid char for json array: " + ch);
            }
        }

        JsonArray jsonArray = new JsonArray(values.toArray(new JsonValue[0]));

        return jsonArray;
    }

    public JsonObject parseJsonObject() {
        Character ch = null;

        testHasNext();

        if (getNextToken() != JsonConstants.LEFT_CURLY_BRACKET) {
            throw new JsonException(String.format("Invalid start char %c at pos %d for json object",
                    json.charAt(index), index));
        }

        ++index;

        JsonObject jsonObject = new JsonObject();

        while (hasNext()) {
            if (getNextToken() == JsonConstants.RIGHT_CURLY_BRACKET) {
                ++index;

                return jsonObject;
            }

            JsonString key = parseJsonString();

            testHasNext();

            if (getNextToken() != JsonConstants.KEY_VALUE_SEPARATOR) {
                throw new JsonException(String.format("Invalid char %s at pos %d for key and value sep for json object",
                        json.charAt(index), index));
            }

            ++index;

            JsonValue value = parse();

            jsonObject.put(new JsonString(key.getValue()), value);

            testHasNext("Missing end } for json object");

            ch = getNextToken();

            if (ch == JsonConstants.COMMA) {
                ++index;
            } else if (ch == JsonConstants.RIGHT_CURLY_BRACKET) {
                ++index;

                return jsonObject;
            } else {
                throw new JsonException("Invalid char for json object as sep or end: " + json.charAt(index));
            }
        }

        throw new JsonException("Missing end } for json object");
    }

    public JsonBoolean parseJsonBoolean() {
        testHasNext();

        Character ch = getNextToken();

        if (ch != 't' && ch != 'f') {
            throw new JsonException(String.format("Invalid char %c at pos %d for json boolean",
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

            if (!JsonBoolean.TRUE_STRING_VALUE.equals(builder.toString())) {
                throw new JsonException(String.format("Invalid true value: %s", builder.toString()));
            }

            return JsonBoolean.TRUE;
        }

        StringBuilder builder = new StringBuilder();

        while (index < json.length() && Character.isLetter(json.charAt(index))) {
            builder.append(json.charAt(index));
            ++index;
        }

        if (!JsonBoolean.FALSE_STRING_VALUE.equals(builder.toString())) {
            throw new JsonException(String.format("Invalid false value: %s", builder.toString()));
        }

        return JsonBoolean.FALSE;
    }

    public JsonNull parseJsonNull() {
        testHasNext();

        if (getNextToken() != 'n') {
            throw new JsonException(String.format("Invalid char %c at pos %d for json null",
                    json.charAt(index), index));
        }

        StringBuilder builder = new StringBuilder();

        while (index < json.length() && Character.isLetter(json.charAt(index))) {
            builder.append(json.charAt(index));
            ++index;
        }

        if (!JsonNull.NULL_STRING_VALUE.equals(builder.toString())) {
            throw new JsonException(String.format("Invalid null value:%s", builder.toString()));
        }

        return JsonNull.NULL;
    }

    public JsonNumber parseJsonNumber() {
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
                return new JsonNumber(Double.parseDouble(builder.toString()));
            } catch (NumberFormatException ex) {

            }

            return new JsonNumber(new BigDecimal(builder.toString()));
        } else {
            try {
                return new JsonNumber(Integer.parseInt(builder.toString()));
            } catch (NumberFormatException ex) {

            }

            try {
                return new JsonNumber(Long.parseLong(builder.toString()));
            } catch (NumberFormatException ex) {

            }

            return new JsonNumber(new BigInteger(builder.toString()));
        }
    }
}

