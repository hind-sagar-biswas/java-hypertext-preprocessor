package com.hindbiswas.jhp.engine;

import java.nio.file.Path;

public class Settings {
    public final Path base;
    public final boolean escapeByDefault;
    public final int maxIncludeDepth;

    public Settings(Path base, boolean escapeByDefault, int maxIncludeDepth) {
        this.base = base;
        this.escapeByDefault = escapeByDefault;
        this.maxIncludeDepth = maxIncludeDepth;
    }

    public Settings(Path base, int maxIncludeDepth) {
        this(base, true, maxIncludeDepth);
    }

    public Settings(Path base) {
        this(base, true, 15);
    }
}
