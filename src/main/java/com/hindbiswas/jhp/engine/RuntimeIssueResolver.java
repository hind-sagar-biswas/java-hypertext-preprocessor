package com.hindbiswas.jhp.engine;


public interface RuntimeIssueResolver {
    /**
     * Handle a runtime issue during rendering.
     *
     * @param type type of issue
     * @param message human-readable message
     * @return mode of rendering (PARTIAL, NONE) or throw exception
     */
    IssueRenderMode handleIssue(IssueType type, String message);
}