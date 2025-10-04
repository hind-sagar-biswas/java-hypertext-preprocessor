package com.hindbiswas.jhp.errors;

import java.nio.file.Path;

public class PathNotInBaseDirectoryException extends RuntimeException {
    public PathNotInBaseDirectoryException(Path path, Path base) {
        super("Path " + path + " is not in base directory " + base);
    }
}
