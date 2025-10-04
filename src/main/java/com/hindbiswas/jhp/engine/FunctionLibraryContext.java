package com.hindbiswas.jhp.engine;

import java.util.List;
import java.util.Deque;
import java.util.Map;

public interface FunctionLibraryContext {
    Object callFunction(String name, List<Object> args, Deque<Map<String, Object>> scopes);
}
