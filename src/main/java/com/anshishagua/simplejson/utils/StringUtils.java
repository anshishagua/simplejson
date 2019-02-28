package com.anshishagua.simplejson.utils;

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
}
