package com.hindbiswas.jhp.engine;

import java.nio.file.Path;

public class Settings {
    public static final class SettingsBuilder {
        private Path base = null;
        private boolean escapeByDefault = defaultEscapeMode;
        private int maxIncludeDepth = defaultMaxIncludeDepth;
        private IssueHandleMode issueHandleMode = defaultIssueHandleMode;

        public SettingsBuilder base(String base) {
            this.base = Path.of(base);
            return this;
        }

        public SettingsBuilder escape(boolean escapeByDefault) {
            this.escapeByDefault = escapeByDefault;
            return this;
        }

        public SettingsBuilder maxIncludeDepth(int maxIncludeDepth) {
            this.maxIncludeDepth = maxIncludeDepth;
            return this;
        }

        public SettingsBuilder issueHandleMode(IssueHandleMode issueHandleMode) {
            this.issueHandleMode = issueHandleMode;
            return this;
        }

        public Settings build() {
            if (base == null) {
                base = Path.of(".");
            }
            return new Settings(base, escapeByDefault, maxIncludeDepth, issueHandleMode);
        }
    }

    public static int defaultMaxIncludeDepth = 15;
    public static boolean defaultEscapeMode = true;
    public static IssueHandleMode defaultIssueHandleMode = IssueHandleMode.COMMENT;

    public final Path base;
    public final boolean escapeByDefault;
    public final int maxIncludeDepth;
    public final IssueHandleMode issueHandleMode;

    private Settings(Path base, boolean escapeByDefault, int maxIncludeDepth,
            IssueHandleMode issueHandleMode) {
        this.base = base;
        this.escapeByDefault = escapeByDefault;
        this.maxIncludeDepth = maxIncludeDepth;
        this.issueHandleMode = issueHandleMode;
    }

    public static SettingsBuilder builder() {
        return new SettingsBuilder();
    }
}
