package com.anshishagua.simplejson.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JSONUtils {
    public static String load(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

        StringBuilder builder = new StringBuilder();

        for (String line : lines) {
            builder.append(line).append(" ");
        }

        return builder.toString();
    }
}
