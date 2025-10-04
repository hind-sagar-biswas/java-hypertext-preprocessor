package com.hindbiswas.jhp.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Instant;
import java.util.*;

@DisplayName("FunctionLibrary Tests")
class FunctionLibraryTest {

    private FunctionLibrary library;
    private Deque<Map<String, Object>> scopes;

    @BeforeEach
    void setUp() {
        library = new FunctionLibrary();
        scopes = new ArrayDeque<>();
        scopes.push(new HashMap<>());
    }

    @Test
    @DisplayName("Should call now() function")
    void testNowFunction() {
        Object result = library.callFunction("now", Collections.emptyList(), scopes);
        assertInstanceOf(Instant.class, result);
    }

    @Test
    @DisplayName("Should call toUpperCase() function")
    void testToUpperCaseFunction() {
        List<Object> args = List.of("hello world");
        Object result = library.callFunction("touppercase", args, scopes);
        assertEquals("HELLO WORLD", result);
    }

    @Test
    @DisplayName("Should call toLowerCase() function")
    void testToLowerCaseFunction() {
        List<Object> args = List.of("HELLO WORLD");
        Object result = library.callFunction("tolowercase", args, scopes);
        assertEquals("hello world", result);
    }

    @Test
    @DisplayName("Should call trim() function")
    void testTrimFunction() {
        List<Object> args = List.of("  hello world  ");
        Object result = library.callFunction("trim", args, scopes);
        assertEquals("hello world", result);
    }

    @Test
    @DisplayName("Should call len() function with string")
    void testLenFunctionWithString() {
        List<Object> args = List.of("hello");
        Object result = library.callFunction("len", args, scopes);
        assertEquals(5, result);
    }

    @Test
    @DisplayName("Should call len() function with collection")
    void testLenFunctionWithCollection() {
        List<Object> args = List.of(Arrays.asList(1, 2, 3, 4, 5));
        Object result = library.callFunction("len", args, scopes);
        assertEquals(5, result);
    }

    @Test
    @DisplayName("Should call len() function with map")
    void testLenFunctionWithMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        List<Object> args = List.of(map);
        Object result = library.callFunction("len", args, scopes);
        assertEquals(3, result);
    }

    @Test
    @DisplayName("Should call len() function with array")
    void testLenFunctionWithArray() {
        List<Object> args = List.of(new int[]{1, 2, 3, 4});
        Object result = library.callFunction("len", args, scopes);
        assertEquals(4, result);
    }

    @Test
    @DisplayName("Should handle null argument in toUpperCase")
    void testToUpperCaseWithNull() {
        List<Object> args = Collections.singletonList(null);
        Object result = library.callFunction("touppercase", args, scopes);
        assertNull(result);
    }

    @Test
    @DisplayName("Should handle empty arguments in len")
    void testLenWithNull() {
        List<Object> args = Collections.singletonList(null);
        Object result = library.callFunction("len", args, scopes);
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Should register custom function")
    void testRegisterCustomFunction() {
        FunctionLibrary.JhpFunction customFn = (args, s) -> "custom result";
        library.register("customFunc", customFn);
        
        assertTrue(library.hasFunction("customFunc"));
        Object result = library.callFunction("customFunc", Collections.emptyList(), scopes);
        assertEquals("custom result", result);
    }

    @Test
    @DisplayName("Should unregister custom function")
    void testUnregisterCustomFunction() {
        FunctionLibrary.JhpFunction customFn = (args, s) -> "custom result";
        library.register("customFunc", customFn);
        assertTrue(library.hasFunction("customFunc"));
        
        library.unregister("customFunc");
        assertFalse(library.hasFunction("customFunc"));
    }

    @Test
    @DisplayName("Should override built-in function with custom function")
    void testOverrideBuiltInFunction() {
        // Register a custom function with the same name as a built-in
        FunctionLibrary.JhpFunction customTrim = (args, s) -> "TRIMMED";
        library.register("trim", customTrim);
        
        List<Object> args = List.of("  hello  ");
        Object result = library.callFunction("trim", args, scopes);
        assertEquals("TRIMMED", result);
    }

    @Test
    @DisplayName("Should throw exception for non-existent function")
    void testNonExistentFunction() {
        assertThrows(IllegalArgumentException.class, () -> {
            library.callFunction("nonExistent", Collections.emptyList(), scopes);
        });
    }

    @Test
    @DisplayName("Should throw exception for null function name")
    void testNullFunctionName() {
        assertThrows(IllegalArgumentException.class, () -> {
            library.callFunction(null, Collections.emptyList(), scopes);
        });
    }

    @Test
    @DisplayName("Should throw exception when registering null function name")
    void testRegisterNullFunctionName() {
        FunctionLibrary.JhpFunction fn = (args, s) -> "result";
        assertThrows(IllegalArgumentException.class, () -> {
            library.register(null, fn);
        });
    }

    @Test
    @DisplayName("Should throw exception when registering empty function name")
    void testRegisterEmptyFunctionName() {
        FunctionLibrary.JhpFunction fn = (args, s) -> "result";
        assertThrows(IllegalArgumentException.class, () -> {
            library.register("", fn);
        });
    }

    @Test
    @DisplayName("Should throw exception when registering null function")
    void testRegisterNullFunction() {
        assertThrows(IllegalArgumentException.class, () -> {
            library.register("myFunc", null);
        });
    }

    @Test
    @DisplayName("Should be case-insensitive for function names")
    void testCaseInsensitiveFunctionNames() {
        assertTrue(library.hasFunction("TOUPPERCASE"));
        assertTrue(library.hasFunction("ToUpperCase"));
        assertTrue(library.hasFunction("touppercase"));
        
        List<Object> args = List.of("hello");
        assertEquals("HELLO", library.callFunction("TOUPPERCASE", args, scopes));
        assertEquals("HELLO", library.callFunction("ToUpperCase", args, scopes));
    }

    @Test
    @DisplayName("Should handle function that throws exception")
    void testFunctionThrowsException() {
        FunctionLibrary.JhpFunction errorFn = (args, s) -> {
            throw new RuntimeException("Test error");
        };
        library.register("errorFunc", errorFn);
        
        assertThrows(RuntimeException.class, () -> {
            library.callFunction("errorFunc", Collections.emptyList(), scopes);
        });
    }

    @Test
    @DisplayName("Should return previous function when registering with same name")
    void testRegisterReturnsPrevious() {
        FunctionLibrary.JhpFunction fn1 = (args, s) -> "result1";
        FunctionLibrary.JhpFunction fn2 = (args, s) -> "result2";
        
        FunctionLibrary.JhpFunction previous = library.register("myFunc", fn1);
        assertNull(previous);
        
        previous = library.register("myFunc", fn2);
        assertNotNull(previous);
        assertEquals(fn1, previous);
    }
}
