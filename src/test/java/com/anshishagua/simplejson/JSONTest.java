package com.anshishagua.simplejson;

import com.anshishagua.simplejson.utils.JSONUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JSONTest {
    @Test
    public void testEmptyJSONArray() {
        String json = "[]";

        Object value = JSON.parse(json);

        Assert.assertEquals("[]", value.toString());
    }

    @Test
    public void testEmptyJSONObject() {
        String json = "{}";

        Object value = JSON.parse(json);

        Assert.assertEquals("{}", value.toString());
    }

    @Test
    public void testJSONNull() {
        String json = "null";

        Object value = JSON.parse(json);

        Assert.assertEquals("null", value.toString());
    }

    @Test
    public void testFormat() {
        String json = "{\"b\": {\"a\": 111, \"b\": [1, 3, 3]}, \"c\": [1, 3,   5], \"d\": false}";

        System.out.println(JSON.format(json));
    }

    @Test
    public void testToJSONString() {
        Person person = new Person();
        person.setId(111);
        person.setName("bbbb");
        person.setPersons(Arrays.asList(new Person(333, "aaaaaa")));

        System.out.println(JSON.toJSONString(person));
    }

    @Test
    public void testParseJSON() throws IOException {
        String json = "{\"id\": 111, \"name\": \"bbbb\",\"success\": true, \"fff\": null}";

        //Set person = JSON.parse(json, Set.class);

        //System.out.println(person);

        json = "[1, 3, 5]";

        System.out.println(JSON.parse(json, List.class));

        Set<Integer> a = new HashSet<>();
        a.add(1);

        System.out.println(a.getClass().getComponentType());

        json = JSONUtils.load(Paths.get("/Users/xiaoli/IdeaProjects/simplejson/src/main/resources/a.json"));

        System.out.println(JSON.parse(json));
    }
}
