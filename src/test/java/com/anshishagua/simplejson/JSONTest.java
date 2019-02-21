package com.anshishagua.simplejson;

import org.junit.Assert;
import org.junit.Test;

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
        String json = "[1,2,3]";

        System.out.println(JSON.format(json));
    }
}
