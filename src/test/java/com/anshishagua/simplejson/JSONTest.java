package com.anshishagua.simplejson;

import com.anshishagua.simplejson.utils.JSONUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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

        double [] a = {1.234234, 3, 5};
        System.out.println(JSON.toJSONString(a));
    }

    @Test
    public void testParseJSON() throws IOException {
        String json = "{\"id\": 111, \"name\": \"bbbb\",\"success\": true, \"fff\": null}";

        json = JSONUtils.loadResource("person.json");
        System.out.println(json);
        Person person = JSON.parse(json, Person.class);

        System.out.println(person);

        json = "[1, 3, 5]";

        System.out.println(Person.class.getFields().length);
    }

    @Test
    public void testParseToObject() {
        double [] a = {1.234234, 3, 5};

        Double[] array = JSON.parse("[1, 222]", Double[].class);

        String [] sss = JSON.parse("[\"aaa\", \"bbb\"]", String[].class);

        System.out.println(Arrays.toString(sss));
    }
}
