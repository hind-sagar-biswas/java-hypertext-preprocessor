package com.hindbiswas.jhp.errors;

import java.nio.file.Path;

public class InvalidFileTypeException extends RuntimeException {
    public InvalidFileTypeException(Path path) {
        super("The included file " + path + " is not a .jhp template file.");
    }
}
