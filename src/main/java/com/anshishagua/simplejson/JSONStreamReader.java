package com.anshishagua.simplejson;

import com.anshishagua.simplejson.types.JSONArray;
import com.anshishagua.simplejson.types.JSONBoolean;
import com.anshishagua.simplejson.types.JSONNull;
import com.anshishagua.simplejson.types.JSONNumber;
import com.anshishagua.simplejson.types.JSONObject;
import com.anshishagua.simplejson.types.JSONString;
import com.anshishagua.simplejson.types.JSONValue;
import com.anshishagua.simplejson.utils.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class JSONStreamReader {
    private static final int DEFAULT_BUF_SIZE = 1024;

    private InputStreamReader reader;

    private char[] buffer;
    private int bufferSize = DEFAULT_BUF_SIZE;
    private int size;
    private int position = -1;

    private void putback() {
        --position;
    }

    public char readNonEmptyChar() {
        char ch = ' ';

        while (true) {
            ch = read();

            if (!StringUtils.isSpaceChar(ch)) {
                return ch;
            }
        }
    }

    private void fillBufferIfNeed() {
        if (position == -1 || position == size) {
            try {
                size = reader.read(buffer, 0, bufferSize);
                position = 0;
            } catch (IOException ex) {
                throw new JSONException(ex);
            }

            if (size == -1) {
                throw new JSONException("EOF read");
            }
        }
    }

    public char read() {
        fillBufferIfNeed();

        char ch = buffer[position++];

        return ch;
    }

    private void expect(char expect) {
        char ch = read();

        if (ch != expect) {
            throw new JSONException("Not expect char: " + ch + ", expecting " + expect);
        }
    }

    private void expectNonEmptyChar(char expect) {
        char ch = readNonEmptyChar();

        if (ch != expect) {
            throw new JSONException("Not expect char: " + ch + ", expecting " + expect);
        }
    }

    public JSONStreamReader(InputStreamReader reader) {
        this.reader = reader;
        this.buffer = new char[bufferSize];
    }

    public JSONStreamReader(InputStream inputStream, Charset charset) {
        this(new InputStreamReader(inputStream, charset));
    }

    public JSONStreamReader(InputStream inputStream, String charsetName) {
        Charset charset = null;

        try {
            charset = Charset.forName(charsetName);
        } catch (Exception ex) {
            throw new JSONException(ex);
        }

        this.reader = new InputStreamReader(inputStream, charset);
        this.buffer = new char[bufferSize];
    }

    public JSONArray parseArray(char first) {
        if (first != '[') {
            throw new JSONException("JSON array not start with [");
        }

        JSONArray jsonArray = new JSONArray();

        char ch = ' ';

        while (true) {
            ch = readNonEmptyChar();

            if (ch == ']') {
                break;
            } else if (ch == ',') {
                continue;
            } else {
                jsonArray.add(parse(ch));
            }
        }

        return jsonArray;
    }

    public JSONObject parseObject(char first) {
        if (first != '{') {
            throw new JSONException("JSON object not start with {");
        }

        JSONObject jsonObject = new JSONObject();

        while (true) {
            char ch = readNonEmptyChar();

            if (ch == '}') {
                break;
            } else if (ch == ',') {
                continue;
            }

            JSONString key = parseString(ch);

            expectNonEmptyChar(':');

            JSONValue value = parse();

            jsonObject.put(key, value);
        }

        return jsonObject;
    }

    public JSONString parseString() {
        return parseString(readNonEmptyChar());
    }

    public JSONString parseString(char first) {
        if (first != '"') {
            throw new JSONException("JSON string not start with \"");
        }

        StringBuilder builder = new StringBuilder();

        char ch = ' ';

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
                    return new JSONString(builder.toString());

                default:
                    builder.append(ch);
            }
        }

        throw new JSONException("No end \"");
    }

    public JSONNumber parseNumber(char first) {
        StringBuilder builder = new StringBuilder();

        builder.append(first);

        while (true) {
            char ch = read();

            boolean shouldBreak = false;

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

        putback();

        if (builder.indexOf(".") > 0) {
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

    public JSONBoolean parseBoolean(char first) {
        // match true
        if (first == 't') {
            expect('r');
            expect('u');
            expect('e');

            return JSONBoolean.TRUE;
        }

        // match false
        expect('a');
        expect('l');
        expect('s');
        expect('e');

        return JSONBoolean.FALSE;
    }

    public JSONNull parseNull(char first) {
        if (first != 'n') {
            throw new JSONException("JSON null not start with n");
        }
        expect('u');
        expect('l');
        expect('l');

        return JSONNull.NULL;
    }

    private JSONValue parse(char ch) {
        switch (ch) {
            case '{':
                return parseObject(ch);
            case '[':
                return parseArray(ch);
            case 'n':
                return parseNull(ch);
            case 't':
            case 'f':
                return parseBoolean(ch);
            case '"':
                return parseString(ch);
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
                return parseNumber(ch);
            default:
                throw new JSONException("Not match char: " + ch);
        }
    }

    public JSONValue parse() {
        char ch = readNonEmptyChar();

        return parse(ch);
    }

    public static void main(String [] args) throws Exception {
        InputStream inputStream = new FileInputStream("/Users/xiaoli/IdeaProjects/simplejson/src/main/resources/taobao.json");

        JSONStreamReader reader = new JSONStreamReader(inputStream, StandardCharsets.UTF_8);

        JSONValue jsonValue = reader.parse();

        System.out.println(jsonValue.format(new FormatConfig(true, 0)));
    }
}
