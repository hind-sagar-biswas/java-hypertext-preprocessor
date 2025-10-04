package com.hindbiswas.jhp.engine;

import com.hindbiswas.jhp.ast.AstBuilder;
import com.hindbiswas.jhp.ast.AstRenderer;
import com.hindbiswas.jhp.ast.TemplateNode;
import com.hindbiswas.jhp.errors.PathNotInBaseDirectoryException;
import com.hindbiswas.jhp.JhpTemplateLexer;
import com.hindbiswas.jhp.JhpTemplateParser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.tree.ParseTree;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class JhpEngine {

    private final class IncludePathResolver implements PathResolver {
        @Override
        public Path resolve(String raw, Path includingFileDir) throws PathNotInBaseDirectoryException {
            Path p = Path.of(raw);
            if (!p.isAbsolute()) {
                if (includingFileDir != null)
                    p = includingFileDir.resolve(p);
                else if (settings.base != null)
                    p = settings.base.resolve(p);
            }
            p = p.normalize();

            // ensure jhp extension: optionally append .jhp if missing
            if (!p.getFileName().toString().endsWith(".jhp")) {
                p = p.resolveSibling(p.getFileName().toString() + ".jhp");
            }

            // security: ensure inside settings.base if settings.base is set
            if (settings.base != null) {
                Path baseNorm = settings.base.toAbsolutePath().normalize();
                if (!p.toAbsolutePath().normalize().startsWith(baseNorm)) {
                    throw new PathNotInBaseDirectoryException(p, settings.base);
                }
            }

            if (p == null) {
                throw new PathNotInBaseDirectoryException(p, settings.base);
            }
            return p;
        }
    }

    private final class AstParser implements PathToAstParser {
        @Override
        public TemplateNode parse(Path path) throws Exception {
            Path normalized = path.toAbsolutePath().normalize();

            // check cache
            synchronized (astCache) {
                TemplateNode cached = astCache.get(normalized);
                if (cached != null)
                    return cached;
            }

            String text = Files.readString(normalized);
            CharStream stream = CharStreams.fromString(text);

            // parse
            JhpTemplateLexer lexer = new JhpTemplateLexer(stream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JhpTemplateParser parser = new JhpTemplateParser(tokens);

            // error handling
            parser.removeErrorListeners();
            parser.addErrorListener(new DiagnosticErrorListener());

            // parse
            ParseTree tree = parser.template();

            // build AST
            AstBuilder builder = new AstBuilder();
            TemplateNode ast = (TemplateNode) builder.visit(tree);

            // update cache
            synchronized (astCache) {
                astCache.put(normalized, ast);
            }
            return ast;
        }
    }

    private final AstParser parser = new AstParser();
    private final Settings settings;
    private final FunctionLibrary functionLibrary;
    private final RuntimeIssueResolver issueResolver;

    // AST cache
    private final Map<Path, TemplateNode> astCache = new HashMap<>();

    public JhpEngine(Settings settings, FunctionLibrary functionLibrary, RuntimeIssueResolver issueResolver) {
        this.settings = settings;
        this.functionLibrary = functionLibrary;
        this.issueResolver = issueResolver;
    }

    public String render(Path templatePath, Map<String, Object> context) throws Exception {
        TemplateNode ast = parser.parse(templatePath);
        AstRenderer renderer = new AstRenderer(settings, functionLibrary, issueResolver, new IncludePathResolver(), parser);
        return renderer.render(ast, context);
    }
}
