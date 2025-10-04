package com.hindbiswas.jhp.engine;

import java.nio.file.Path;

import com.hindbiswas.jhp.ast.TemplateNode;

@FunctionalInterface
public interface PathToAstParser {
    public TemplateNode parse(Path path) throws Exception;   
}
