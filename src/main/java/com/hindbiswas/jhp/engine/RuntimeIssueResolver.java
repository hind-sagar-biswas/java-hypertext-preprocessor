package com.hindbiswas.jhp.engine;

import java.nio.file.Path;
import java.util.Deque;

public interface RuntimeIssueResolver {
    /**
     * Handle a runtime issue during rendering.
     *
     * @param type type of issue
     * @param message human-readable message
     * @return mode of rendering (PARTIAL, NONE) or throw exception
     */
    void handle(IssueType type, String message, StringBuilder sb, ThreadLocal<Deque<Path>> includeStack);
}