package com.anshishagua.simplejson;

import com.anshishagua.simplejson.types.JSONArray;
import com.anshishagua.simplejson.types.JSONNumber;
import com.anshishagua.simplejson.types.JSONValue;
import com.anshishagua.simplejson.utils.JSONUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class JSONTest {
    @Test
    public void testEmptyJSONArray() {
        String json = "[{\n\"aa\":   11}]";

        Object value = JSON.parse(json);

        //Assert.assertEquals("[]", value.toString());
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

        System.out.println(JSON.parse("\"2018-01-01 11:11:11\"", LocalDateTime.class));
    }

    @Test
    public void aaaa() throws IOException {
        String json = JSONUtils.loadResource("person.json");

        System.out.println(JSON.parse(json, Person.class));

        System.out.println(JSON.format(JSONUtils.loadResource("test.json")));

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
        person.setRole(Person.Role.USER);
        //person.setParents(Arrays.asList(person));

        json = JSON.toJSONString(person);

        /*
        JSONValue jsonValue = JSON.parse(json);
        System.out.println(JSON.toJSONString(person));

        IdentityHashMap<Object, Object> map;

        System.out.println(JSON.format("[{}, {}]"));
        */
    }

    @Test
    public void testCompress() throws Exception {
        int [] a = {1, 2, 3};

        Object b = a;

        System.out.println(b);

        int [] c = (int[]) b;

        System.out.println(c[0]);

        System.out.println((char) 9);
        System.out.println(Integer.parseInt("+1"));
        Character ch = '1';
        System.out.println(ch == '1');
        System.out.println(Character.isSpaceChar('\n'));
        String json = JSONUtils.loadResource("taobao.json");
        //json = JSON.format(json);
        //System.out.println(json);
        List list = new ArrayList();
        list.add(Arrays.asList(1, 3, 4));
        Map<String, Object> map = new HashMap<>();
        map.put("1", 1111);
        map.put("2", Arrays.asList(3.5, 2.3));

        list.add(map);

        Person person = new Person();
        person.setRole(Person.Role.ADMIN);
        person.setBirthday(LocalDate.now());
        person.setName("aa");

        Address [] addresses = new Address[1];
        Address address = new Address();
        address.setStreet("aaa");
        address.setCountry("China");
        address.setCity("beijing");
        addresses[0] = address;

        person.setAddresses(addresses);

        list.add(person);

        json = JSON.toJSONString(list);
        System.out.println(json);

        JSONArray jsonArray = JSON.parseArray(json);

        System.out.println(jsonArray.format());
        //System.out.println(JSON.compress(json));
    }

    @Test
    public void testAAA() throws Exception {
        JSONValue jsonValue = new JSONNumber(1);

        System.out.println(JSON.toJSONString(1));

        Field [] fields = Person.class.getDeclaredFields();

        Field field = Person.class.getDeclaredField("id");

        Person person = new Person();
        person.setId(1111);

        field.setAccessible(true);
        System.out.println(field.getInt(person));

        int [][] a = {{1, 3, 5}, {}, {3}};

        String json = JSON.toJSONString(a);

        System.out.println(json);

        int[][] b = JSON.parse(json, int[][].class);

        System.out.println(Arrays.toString(b));

        System.out.println(State.class.getMethod("name"));

        State s;
        System.out.println(State.class.getSuperclass());
    }
}
