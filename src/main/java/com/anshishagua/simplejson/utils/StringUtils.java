package com.anshishagua.simplejson.utils;

import com.anshishagua.simplejson.JSONConstants;

import java.util.Objects;

public class StringUtils {
    public static String repeat(String string, int count) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < count; ++i) {
            builder.append(string);
        }

        return builder.toString();
    }

    public static String repeat(char ch, int count) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < count; ++i) {
            builder.append(ch);
        }

        return builder.toString();
    }

    public static String doubleQuote(Object object) {
        Objects.requireNonNull(object);

        return String.format("\"%s\"", object.toString());
    }

    public static boolean isSpaceChar(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n' || Character.isSpaceChar(ch);
    }

    public static boolean isJSONUnicode(String string) {
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

    public static boolean isValidEscapeChar(char ch) {
        return  ch == JSONConstants.DOUBLE_QUOTE ||
                ch == JSONConstants.BACK_CHAR ||
                ch == JSONConstants.FORMAT_CHAR ||
                ch == JSONConstants.TAB ||
                ch == JSONConstants.RETURN_CHAR ||
                ch == JSONConstants.NEW_LINE ||
                ch == JSONConstants.ESCAPE_CHAR ||
                ch == JSONConstants.LEFT_SLASH;
    }
}
