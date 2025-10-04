package com.hindbiswas.jhp.engine;


public interface RuntimeIssueResolver {
    /**
     * Handle a runtime issue during rendering.
     *
     * @param type type of issue
     * @param message human-readable message
     * @return mode of rendering (PARTIAL, NONE) or throw exception
     */
    IssueRenderMode handle(IssueType type, String message, StringBuilder sb);
}