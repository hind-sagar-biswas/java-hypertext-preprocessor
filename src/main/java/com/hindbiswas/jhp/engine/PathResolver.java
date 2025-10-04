package com.hindbiswas.jhp.engine;

import java.nio.file.Path;

@FunctionalInterface
public interface PathResolver {
    public Path resolve(String path, Path includingFileDir) throws Exception;   
}
