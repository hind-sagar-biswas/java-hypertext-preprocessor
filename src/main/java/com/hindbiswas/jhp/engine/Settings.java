package com.hindbiswas.jhp.engine;

import java.nio.file.Path;

public class Settings {
    public final Path baseDir;
    public final boolean htmlEscape;
    public final int maxIncludeDepth;

    public Settings(Path baseDir, boolean htmlEscape, int maxIncludeDepth) {
        this.baseDir = baseDir;
        this.htmlEscape = htmlEscape;
        this.maxIncludeDepth = maxIncludeDepth;
    }

    public Settings(Path baseDir, int maxIncludeDepth) {
        this(baseDir, true, maxIncludeDepth);
    }

    public Settings(Path baseDir) {
        this(baseDir, true, 15);
    }
}
