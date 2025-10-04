package com.hindbiswas.jhp.engine;

import java.nio.file.Path;
import java.util.Map;

import com.hindbiswas.jhp.ast.AstRenderer;
import com.hindbiswas.jhp.ast.TemplateNode;

public class TemplateEngine {
    private final Settings settings;
    private final FunctionLibrary functionLibrary;
    private final RuntimeIssueResolver issueResolver;

    public TemplateEngine(Settings settings, FunctionLibrary functionLibrary, RuntimeIssueResolver issueResolver) {
        this.settings = settings;
        this.functionLibrary = functionLibrary;
        this.issueResolver = issueResolver;
    }

    public String render(Path path, Map<String, Object> context) throws Exception {
        TemplateNode ast = parse(path);
        AstRenderer renderer = new AstRenderer(settings.baseDir);
        return renderer.render(ast, context);
    }

    private TemplateNode parse(Path templatePath) throws Exception {
        throw new UnsupportedOperationException();
    }
}
