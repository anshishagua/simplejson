package com.anshishagua.simplejson;

import com.anshishagua.simplejson.types.JSONArray;
import com.anshishagua.simplejson.types.JSONBoolean;
import com.anshishagua.simplejson.types.JSONNull;
import com.anshishagua.simplejson.types.JSONNumber;
import com.anshishagua.simplejson.types.JSONObject;
import com.anshishagua.simplejson.types.JSONString;
import com.anshishagua.simplejson.types.JSONValue;
import com.anshishagua.simplejson.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

class JSONScanner2 {
    private static final char EOF = (char) -1;

    private static final String NUMBER_PATTERN_STRING = "-?(0|[1-9][0-9]*)(\\.[0-9]+)?([Ee][+\\-]?(0|[1-9][0-9]*))?";
    private static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER_PATTERN_STRING);

    private String json;
    private int position;

    public JSONScanner2(String json) {
        this.json = json;
    }

    public int getPosition() {
        return position;
    }

    public char read() {
        return ' ';
    }

    public char readNonEmptyChar() {
        return ' ';
    }

    private void expect(char expect) {
        char ch = read();

        if (ch != expect) {
            throw new JSONException("Not expect char: " + ch + ", expecting " + expect);
        }
    }

    public JSONValue parse2() {
        Stack<Object> stack = new Stack<>();
        Stack<State> states = new Stack<>();
        State state = State.BEGIN;

        Type type;

        while (true) {
            char ch = readNonEmptyChar();

            if (ch == EOF) {
                if (stack.size() != 1) {
                    throw new JSONException();
                }
            }

            if (ch == '{') {
                stack.push("{");
                states.push(State.OBJECT_BEGIN);
                ++position;
            } else if (ch == '[') {
                stack.push(new JSONArray());
                states.push(State.ARRAY_BEGIN);
                ++position;
            } else if (ch == '"') {

            } else if (ch == ',') {
                if (state == State.OBJECT_BEGIN) {
                    state = State.OBJECT_EXPECTING_KEY;
                } else if (state == State.ARRAY_BEGIN) {
                    state = State.ARRAY_EXPECTING_ITEM;
                } else {

                }
            } else if (ch == ':') {
                if (state == State.OBJECT_EXPECTING_KEY) {
                    state = State.OBJECT_EXPECTING_VALUE;
                } else {

                }
            } else if (ch == ']') {
                if (state == State.ARRAY_EXPECTING_ITEM) {

                }


            } else if (ch == '}') {

            }
        }
    }

    public JSONValue parse() {
        State state = State.BEGIN;
        StringBuilder builder = new StringBuilder();
        JSONObject jsonObject;
        JSONArray jsonArray;
        boolean shouldBreak = false;

        Stack<Object> stack = new Stack<>();
        char ch = ' ';

        switch (state) {
            case BEGIN:
                ch = readNonEmptyChar();

                switch (ch) {
                    case '{':
                        state = State.OBJECT_BEGIN;
                        stack.push("{");
                        break;
                    case '"':
                        state = State.STRING_BEGIN;
                        break;
                    case '[':
                        stack.push(state);
                        state = State.ARRAY_BEGIN;
                        break;
                    case ',':
                        state = State.BEGIN;
                        break;
                    case ':':
                        state = State.OBJECT_EXPECTING_VALUE;
                        break;
                    case 'n':
                        expect('u');
                        expect('l');
                        expect('l');

                        stack.push(JSONNull.NULL);

                        if (state == State.OBJECT_BEGIN) {

                        } else if (state == State.ARRAY_BEGIN) {

                        } else {

                        }
                }
                break;

            case END:
                break;

        }

        JSONValue jsonValue = null;

        switch (ch) {
            case '{':
                stack.push("{");
                state = State.OBJECT_BEGIN;
                break;
            case '"':
                builder = new StringBuilder();

                while ((ch = read()) != -1) {
                    switch (ch) {
                        case '\\':
                            char escapeChar = read();

                            switch (escapeChar) {
                                case '"':
                                case 'b':
                                case 'r':
                                case 'n':
                                case 't':
                                case 'f':
                                case '/':
                                case '\\':
                                    builder.append(ch);
                                    break;
                                case 'u':
                                    StringBuilder unicode = new StringBuilder(4);
                                    builder.append(read());
                                    builder.append(read());
                                    builder.append(read());
                                    builder.append(read());

                                    if (!StringUtils.isJSONUnicode(unicode.toString())) {
                                        throw new JSONException("Not valid unicode:" + unicode);
                                    }

                                    builder.append((char) Integer.parseInt(unicode.toString(), 16));
                                    break;
                                default:
                                    throw new JSONException("Invalid escape char " + escapeChar + " in string");
                            }
                        case '"':
                            stack.push(new JSONString(builder.toString()));

                            shouldBreak = true;
                            break;
                        default:
                            builder.append(ch);
                    }

                    if (shouldBreak) {
                        break;
                    }
                }
                break;
            case '[':
                break;
            case 't':
                expect('r');
                expect('u');
                expect('e');
                stack.push(JSONBoolean.TRUE);

                break;
            case 'f':
                expect('a');
                expect('l');
                expect('s');
                expect('e');

                stack.push(JSONBoolean.FALSE);
                break;
            case 'n':
                expect('u');
                expect('l');
                expect('l');

                stack.push(JSONNull.NULL);
                break;
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                builder = new StringBuilder();
                builder.append(ch);

                while (true) {
                    ch = read();

                    shouldBreak = false;

                    switch (ch) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                        case '+':
                        case '-':
                        case 'e':
                        case 'E':
                            builder.append(ch);
                            break;
                        default:
                            shouldBreak = true;
                            break;
                    }

                    if (shouldBreak) {
                        break;
                    }
                }

                --position;

                JSONNumber jsonNumber = null;

                if (builder.indexOf(".") > 0) {
                    try {
                        jsonNumber = new JSONNumber(Double.parseDouble(builder.toString()));
                    } catch (NumberFormatException ex) {

                    }

                    jsonNumber = new JSONNumber(new BigDecimal(builder.toString()));
                } else {
                    try {
                        jsonNumber = new JSONNumber(Integer.parseInt(builder.toString()));
                    } catch (NumberFormatException ex) {

                    }

                    try {
                        jsonNumber = new JSONNumber(Long.parseLong(builder.toString()));
                    } catch (NumberFormatException ex) {

                    }

                    jsonNumber = new JSONNumber(new BigInteger(builder.toString()));
                }

                stack.push(jsonNumber);
                break;
            default:
                throw new JSONException(String.format("Not valid char %c at pos:%d", ch, position));
        }

        return jsonValue;
    }

    public static void main(String [] args) {
        List<int[]> list = new ArrayList<>();

        list = JSON.parse("[[1, 3], [3, 5]]", list.getClass());

        Type type = list.getClass();

        Field field;

        System.out.println(list.getClass().getTypeParameters()[0].getClass().getTypeName());

        type = Integer[].class;
        System.out.println(type instanceof GenericArrayType);

        URL url = ClassLoader.getSystemResource("a.json");

        System.out.println(url);

        System.out.println(JSONScanner2.class.getClassLoader());
        System.out.println(int.class.getClassLoader());
        System.out.println(JSONScanner2.class.getPackage().getImplementationVersion());
    }
}

