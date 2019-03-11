package com.anshishagua.simplejson;

import com.anshishagua.simplejson.types.JsonArray;
import com.anshishagua.simplejson.types.JsonBoolean;
import com.anshishagua.simplejson.types.JsonNull;
import com.anshishagua.simplejson.types.JsonNumber;
import com.anshishagua.simplejson.types.JsonObject;
import com.anshishagua.simplejson.types.JsonString;
import com.anshishagua.simplejson.types.JsonValue;
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
                throw new JsonException(ex);
            }

            if (size == -1) {
                throw new JsonException("EOF read");
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
            throw new JsonException("Not expect char: " + ch + ", expecting " + expect);
        }
    }

    private void expectNonEmptyChar(char expect) {
        char ch = readNonEmptyChar();

        if (ch != expect) {
            throw new JsonException("Not expect char: " + ch + ", expecting " + expect);
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
            throw new JsonException(ex);
        }

        this.reader = new InputStreamReader(inputStream, charset);
        this.buffer = new char[bufferSize];
    }

    public JsonArray parseArray(char first) {
        if (first != '[') {
            throw new JsonException("Json array not start with [");
        }

        JsonArray jsonArray = new JsonArray();

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

    public JsonObject parseObject(char first) {
        if (first != '{') {
            throw new JsonException("Json object not start with {");
        }

        JsonObject jsonObject = new JsonObject();

        while (true) {
            char ch = readNonEmptyChar();

            if (ch == '}') {
                break;
            } else if (ch == ',') {
                continue;
            }

            JsonString key = parseString(ch);

            expectNonEmptyChar(':');

            JsonValue value = parse();

            jsonObject.put(key, value);
        }

        return jsonObject;
    }

    public JsonString parseString() {
        return parseString(readNonEmptyChar());
    }

    public JsonString parseString(char first) {
        if (first != '"') {
            throw new JsonException("Json string not start with \"");
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
                                throw new JsonException("Not valid unicode:" + unicode);
                            }

                            builder.append((char) Integer.parseInt(unicode.toString(), 16));
                            break;
                        default:
                            throw new JsonException("Invalid escape char " + escapeChar + " in string");
                    }
                case '"':
                    return new JsonString(builder.toString());

                default:
                    builder.append(ch);
            }
        }

        throw new JsonException("No end \"");
    }

    public JsonNumber parseNumber(char first) {
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

    public JsonBoolean parseBoolean(char first) {
        // match true
        if (first == 't') {
            expect('r');
            expect('u');
            expect('e');

            return JsonBoolean.TRUE;
        }

        // match false
        expect('a');
        expect('l');
        expect('s');
        expect('e');

        return JsonBoolean.FALSE;
    }

    public JsonNull parseNull(char first) {
        if (first != 'n') {
            throw new JsonException("Json null not start with n");
        }
        expect('u');
        expect('l');
        expect('l');

        return JsonNull.NULL;
    }

    private JsonValue parse(char ch) {
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
                throw new JsonException("Not match char: " + ch);
        }
    }

    public JsonValue parse() {
        char ch = readNonEmptyChar();

        return parse(ch);
    }

    public static void main(String [] args) throws Exception {
        InputStream inputStream = new FileInputStream("/Users/xiaoli/IdeaProjects/simplejson/src/main/resources/taobao.json");

        JSONStreamReader reader = new JSONStreamReader(inputStream, StandardCharsets.UTF_8);

        JsonValue jsonValue = reader.parse();

        System.out.println(jsonValue.format(new FormatConfig(true, 0)));
    }
}
