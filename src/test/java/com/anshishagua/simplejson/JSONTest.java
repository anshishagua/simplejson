package com.anshishagua.simplejson;

import com.anshishagua.simplejson.utils.JSONUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.IdentityHashMap;

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

        System.out.println(JSON.toJSONString(Role.ADMIN));

        System.out.println(JSON.parse("\"ADMIN\"", Role.class));

        System.out.println(JSON.parse("\"2018-01-01 11:11:11\"", LocalDateTime.class));
    }

    @Test
    public void aaaa() throws IOException {
        String json = JSONUtils.loadResource("person.json");

        System.out.println(JSON.parse(json, Person.class));

        System.out.println(JSON.parseObject(JSONUtils.loadResource("test.json")));

        Person person = new Person();
        person.setId(1);
        person.setName("benben");
        Address address = new Address();
        address.setCity("Beijing");
        address.setCountry("China");
        address.setStreet("street");
        Address[] addresses = new Address[] {address};
        person.setAddresses(addresses);

        person.setBirthday(LocalDate.now());
        person.setParents(Arrays.asList(person));

        System.out.println(JSON.toJSONString(person));

        IdentityHashMap<Object, Object> map;
    }
}
