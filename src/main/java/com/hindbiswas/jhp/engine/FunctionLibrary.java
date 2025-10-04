package com.hindbiswas.jhp.engine;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FunctionLibrary implements FunctionLibraryContext {

    @FunctionalInterface
    public interface JhpFunction {
        Object apply(List<Object> args, Deque<Map<String, Object>> scopes);
    }

    private final Map<String, JhpFunction> stdLib;
    private final Map<String, JhpFunction> userLib; 

    public FunctionLibrary() {
        this.userLib = new ConcurrentHashMap<>();
        Map<String, JhpFunction> builtins = new HashMap<>();

        // now() -> returns Instant.now()
        builtins.put("now", (args, scopes) -> {
            if (args != null && !args.isEmpty()) {
                // ignore args but be forgiving
            }
            return Instant.now();
        });

        // toUppercase(s)
        builtins.put("touppercase", (args, scopes) -> {
            if (args == null || args.isEmpty() || args.get(0) == null) return null;
            return args.get(0).toString().toUpperCase();
        });

        // toLowerCase(s)
        builtins.put("tolowercase", (args, scopes) -> {
            if (args == null || args.isEmpty() || args.get(0) == null) return null;
            return args.get(0).toString().toLowerCase();
        });

        // trim(s)
        builtins.put("trim", (args, scopes) -> {
            if (args == null || args.isEmpty() || args.get(0) == null) return null;
            return args.get(0).toString().trim();
        });

        // Example: len(s|collection|map)
        builtins.put("len", (args, scopes) -> {
            if (args == null || args.isEmpty() || args.get(0) == null) return 0;
            Object o = args.get(0);
            if (o instanceof CharSequence) return ((CharSequence) o).length();
            if (o instanceof Collection) return ((Collection<?>) o).size();
            if (o instanceof Map) return ((Map<?, ?>) o).size();
            if (o.getClass().isArray()) return java.lang.reflect.Array.getLength(o);
            return 0;
        });

        this.stdLib = Collections.unmodifiableMap(builtins);
    }

    public JhpFunction register(String name, JhpFunction fn) throws IllegalArgumentException {
        validateNameAndFn(name, fn);
        return userLib.put(normalize(name), fn);
    }

    public JhpFunction unregister(String name) {
        if (name == null) return null;
        return userLib.remove(normalize(name));
    }

    public boolean hasFunction(String name) {
        if (name == null) return false;
        String n = normalize(name);
        return userLib.containsKey(n) || stdLib.containsKey(n);
    }

    @Override
    public Object callFunction(String name, List<Object> args, Deque<Map<String, Object>> scopes) {
        if (name == null) throw new IllegalArgumentException("Function name cannot be null");
        String n = normalize(name);

        // priority: userLib -> stdLib
        JhpFunction fn = userLib.get(n);
        if (fn != null) {
            return safeInvoke(fn, args, scopes, n);
        }

        fn = stdLib.get(n);
        if (fn != null) {
            return safeInvoke(fn, args, scopes, n);
        }

        throw new IllegalArgumentException("Function not found: " + name);
    }

    /* ----------------- helpers ----------------- */

    private Object safeInvoke(JhpFunction fn, List<Object> args, Deque<Map<String, Object>> scopes, String name) {
        try {
            return fn.apply(args == null ? Collections.emptyList() : args, scopes);
        } catch (RuntimeException re) {
            // wrap to provide contextual info
            throw new RuntimeException("Error executing function '" + name + "': " + re.getMessage(), re);
        }
    }

    private void validateNameAndFn(String name, JhpFunction fn) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Function name cannot be null/empty");
        }
        if (fn == null) {
            throw new IllegalArgumentException("Function implementation cannot be null");
        }
    }

    private String normalize(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }
}
