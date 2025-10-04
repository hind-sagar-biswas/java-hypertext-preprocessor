package com.hindbiswas.jhp.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.nio.file.Path;

@DisplayName("Settings Tests")
class SettingsTest {

    @Test
    @DisplayName("Should create settings with default values")
    void testDefaultSettings() {
        Settings settings = Settings.builder().build();
        
        assertNotNull(settings.base);
        assertEquals(Path.of("."), settings.base);
        assertTrue(settings.escapeByDefault);
        assertEquals(15, settings.maxIncludeDepth);
        assertEquals(IssueHandleMode.COMMENT, settings.issueHandleMode);
    }

    @Test
    @DisplayName("Should create settings with custom base path")
    void testCustomBasePath() {
        Settings settings = Settings.builder()
            .base("/tmp/templates")
            .build();
        
        assertEquals(Path.of("/tmp/templates"), settings.base);
    }

    @Test
    @DisplayName("Should create settings with escape disabled")
    void testEscapeDisabled() {
        Settings settings = Settings.builder()
            .escape(false)
            .build();
        
        assertFalse(settings.escapeByDefault);
    }

    @Test
    @DisplayName("Should create settings with custom max include depth")
    void testCustomMaxIncludeDepth() {
        Settings settings = Settings.builder()
            .maxIncludeDepth(10)
            .build();
        
        assertEquals(10, settings.maxIncludeDepth);
    }

    @Test
    @DisplayName("Should create settings with THROW issue handle mode")
    void testThrowIssueHandleMode() {
        Settings settings = Settings.builder()
            .issueHandleMode(IssueHandleMode.THROW)
            .build();
        
        assertEquals(IssueHandleMode.THROW, settings.issueHandleMode);
    }

    @Test
    @DisplayName("Should create settings with DEBUG issue handle mode")
    void testDebugIssueHandleMode() {
        Settings settings = Settings.builder()
            .issueHandleMode(IssueHandleMode.DEBUG)
            .build();
        
        assertEquals(IssueHandleMode.DEBUG, settings.issueHandleMode);
    }

    @Test
    @DisplayName("Should create settings with IGNORE issue handle mode")
    void testIgnoreIssueHandleMode() {
        Settings settings = Settings.builder()
            .issueHandleMode(IssueHandleMode.IGNORE)
            .build();
        
        assertEquals(IssueHandleMode.IGNORE, settings.issueHandleMode);
    }

    @Test
    @DisplayName("Should create settings with all custom values")
    void testAllCustomValues() {
        Settings settings = Settings.builder()
            .base("/var/templates")
            .escape(false)
            .maxIncludeDepth(20)
            .issueHandleMode(IssueHandleMode.THROW)
            .build();
        
        assertEquals(Path.of("/var/templates"), settings.base);
        assertFalse(settings.escapeByDefault);
        assertEquals(20, settings.maxIncludeDepth);
        assertEquals(IssueHandleMode.THROW, settings.issueHandleMode);
    }

    @Test
    @DisplayName("Should use builder pattern fluently")
    void testBuilderPattern() {
        Settings settings = Settings.builder()
            .base("/tmp")
            .escape(true)
            .maxIncludeDepth(5)
            .issueHandleMode(IssueHandleMode.COMMENT)
            .build();
        
        assertNotNull(settings);
        assertEquals(Path.of("/tmp"), settings.base);
    }

    @Test
    @DisplayName("Should verify default static values")
    void testDefaultStaticValues() {
        assertEquals(15, Settings.defaultMaxIncludeDepth);
        assertTrue(Settings.defaultEscapeMode);
        assertEquals(IssueHandleMode.COMMENT, Settings.defaultIssueHandleMode);
    }
}
