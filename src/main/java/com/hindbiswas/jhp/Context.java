package com.hindbiswas.jhp;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class Context {
    private final Map<String, Object> context = new HashMap<>();

    public void add(String key, Object value) {
        context.put(key, toContextValue(value));
    }

    public Map<String, Object> getContext() {
        return context;
    }

    private Object toContextValue(Object value) {
        if (value == null)
            return null;

        // Primitive wrapper, String, Map -> store as-is
        if (isPrimitiveOrString(value) || value instanceof Map) {
            return value;
        }

        // Arrays
        if (value.getClass().isArray()) {
            int len = Array.getLength(value);
            Object[] arr = new Object[len];
            for (int i = 0; i < len; i++) {
                arr[i] = toContextValue(Array.get(value, i));
            }
            return arr;
        }

        // Collections
        if (value instanceof Collection<?> coll) {
            List<Object> list = new ArrayList<>();
            for (Object o : coll) {
                list.add(toContextValue(o));
            }
            return list;
        }

        // POJO -> convert public fields only
        Map<String, Object> map = new HashMap<>();
        Field[] fields = value.getClass().getFields(); // only public fields
        for (Field f : fields) {
            try {
                Object fieldValue = f.get(value);
                // skip nested POJOs, only include arrays/collections/primitives
                if (fieldValue != null && !isPrimitiveOrString(fieldValue)
                        && !(fieldValue instanceof Map)
                        && !fieldValue.getClass().isArray()
                        && !(fieldValue instanceof Collection<?>)) {
                    continue;
                }
                map.put(f.getName(), toContextValue(fieldValue));
            } catch (IllegalAccessException e) {
                // ignore inaccessible fields
            }
        }
        return map;
    }

    private boolean isPrimitiveOrString(Object value) {
        return value instanceof String
                || value instanceof Number
                || value instanceof Boolean
                || value instanceof Character
                || value.getClass().isPrimitive();
    }
}
