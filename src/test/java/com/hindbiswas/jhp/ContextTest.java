package com.hindbiswas.jhp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.*;

@DisplayName("Context Tests")
class ContextTest {

    private Context context;

    @BeforeEach
    void setUp() {
        context = new Context();
    }

    @Test
    @DisplayName("Should add and retrieve string value")
    void testAddString() {
        context.add("name", "Alice");
        Map<String, Object> ctx = context.getContext();
        assertEquals("Alice", ctx.get("name"));
    }

    @Test
    @DisplayName("Should add and retrieve number value")
    void testAddNumber() {
        context.add("age", 25);
        Map<String, Object> ctx = context.getContext();
        assertEquals(25, ctx.get("age"));
    }

    @Test
    @DisplayName("Should add and retrieve boolean value")
    void testAddBoolean() {
        context.add("active", true);
        Map<String, Object> ctx = context.getContext();
        assertEquals(true, ctx.get("active"));
    }

    @Test
    @DisplayName("Should handle null values")
    void testAddNull() {
        context.add("nullValue", null);
        Map<String, Object> ctx = context.getContext();
        assertTrue(ctx.containsKey("nullValue"));
        assertNull(ctx.get("nullValue"));
    }

    @Test
    @DisplayName("Should convert POJO to Map")
    void testAddPOJO() {
        TestUser user = new TestUser("Bob", 30);
        context.add("user", user);
        
        Map<String, Object> ctx = context.getContext();
        Object userObj = ctx.get("user");
        
        assertInstanceOf(Map.class, userObj);
        @SuppressWarnings("unchecked")
        Map<String, Object> userMap = (Map<String, Object>) userObj;
        assertEquals("Bob", userMap.get("name"));
        assertEquals(30, userMap.get("age"));
    }

    @Test
    @DisplayName("Should convert array to Object array")
    void testAddArray() {
        int[] numbers = {1, 2, 3, 4, 5};
        context.add("numbers", numbers);
        
        Map<String, Object> ctx = context.getContext();
        Object numbersObj = ctx.get("numbers");
        
        assertInstanceOf(Object[].class, numbersObj);
        Object[] arr = (Object[]) numbersObj;
        assertEquals(5, arr.length);
        assertEquals(1, arr[0]);
    }

    @Test
    @DisplayName("Should convert Collection to List")
    void testAddCollection() {
        List<String> items = Arrays.asList("apple", "banana", "cherry");
        context.add("items", items);
        
        Map<String, Object> ctx = context.getContext();
        Object itemsObj = ctx.get("items");
        
        assertInstanceOf(List.class, itemsObj);
        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) itemsObj;
        assertEquals(3, list.size());
        assertEquals("apple", list.get(0));
    }

    @Test
    @DisplayName("Should preserve Map as-is")
    void testAddMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("key1", "value1");
        data.put("key2", 42);
        
        context.add("data", data);
        
        Map<String, Object> ctx = context.getContext();
        Object dataObj = ctx.get("data");
        
        assertInstanceOf(Map.class, dataObj);
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) dataObj;
        assertEquals("value1", map.get("key1"));
        assertEquals(42, map.get("key2"));
    }

    @Test
    @DisplayName("Should handle multiple additions")
    void testMultipleAdditions() {
        context.add("name", "Alice");
        context.add("age", 25);
        context.add("active", true);
        
        Map<String, Object> ctx = context.getContext();
        assertEquals(3, ctx.size());
        assertEquals("Alice", ctx.get("name"));
        assertEquals(25, ctx.get("age"));
        assertEquals(true, ctx.get("active"));
    }

    @Test
    @DisplayName("Should overwrite existing key")
    void testOverwriteKey() {
        context.add("name", "Alice");
        context.add("name", "Bob");
        
        Map<String, Object> ctx = context.getContext();
        assertEquals("Bob", ctx.get("name"));
    }

    // Helper class for testing POJO conversion
    static class TestUser {
        public String name;
        public int age;

        public TestUser(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
