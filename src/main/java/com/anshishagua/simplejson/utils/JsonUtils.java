package com.anshishagua.simplejson.utils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JsonUtils {
    public static String loadResource(String fileName) throws IOException {
        String file = JsonUtils.class.getClassLoader().getResource(fileName).getPath();

        return load(Paths.get(file));
    }

    public static String load(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

        StringBuilder builder = new StringBuilder();

        for (String line : lines) {
            builder.append(line).append(" ");
        }

        return builder.toString();
    }

    public static void main(String [] args) {
        URL url = JsonUtils.class.getClassLoader().getResource("a.json");
        System.out.println(url.getPath());
    }
}
