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
import java.util.Deque;
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

    private final class IssueResolver implements RuntimeIssueResolver {
        @Override
        public void handle(IssueType type, String message, StringBuilder sb, ThreadLocal<Deque<Path>> includeStack) {
            if (settings.issueHandleMode == IssueHandleMode.COMMENT) {
                sb.append("<!-- ").append(type).append(": ").append(message).append(" -->\n");
            } else if (settings.issueHandleMode == IssueHandleMode.THROW) {
                throw new RuntimeException(type + ": " + message);
            } else if (settings.issueHandleMode == IssueHandleMode.DEBUG) {
                // Build debug info
                String ts = java.time.Instant.now().toString();
                String thread = Thread.currentThread().getName();

                String baseDir = settings.base != null ? settings.base.toString() : "null";
                String escape = Boolean.toString(settings.escapeByDefault);
                String maxDepth = Integer.toString(settings.maxIncludeDepth);
                String mode = settings.issueHandleMode.name();

                // defensive HTML escaping
                String mEsc = escapeHtml(message);
                String baseEsc = escapeHtml(baseDir);

                sb.append("\n<!-- ISSUE DEBUG START -->\n");

                // Collapsible debug panel so users aren't overwhelmed
                sb.append(
                        "<details style=\"font-family:system-ui,monospace;margin:8px 0;border:1px solid #ddd;padding:8px;background:#fff;\">");
                sb.append(
                        "<summary style=\"font-weight:bold;color:#c00;margin-bottom:6px;\">Template Engine Debug — Issue: ")
                        .append(escapeHtml(type.name())).append("</summary>");

                sb.append("<div style=\"margin-top:8px;\">");
                sb.append("<table style=\"border-collapse:collapse;width:100%;font-size:13px;\">");

                appendRow(sb, "Issue Type", escapeHtml(type.name()));
                appendRow(sb, "Message", mEsc);
                appendRow(sb, "Timestamp", ts);
                appendRow(sb, "Thread", escapeHtml(thread));
                appendRow(sb, "Handler Mode", mode);
                appendRow(sb, "Base Dir", baseEsc);
                appendRow(sb, "HTML Escape On", escape);
                appendRow(sb, "Max Include Depth", maxDepth);

                // includeStack context (if the resolver has access)
                try {
                    Deque<Path> stack = includeStack.get();
                    if (stack != null && !stack.isEmpty()) {
                        // render stack as a numbered list
                        StringBuilder stackSb = new StringBuilder();
                        int i = 0;
                        for (Path p : stack) {
                            stackSb.append(++i).append(". ").append(escapeHtml(p.toString())).append("<br/>");
                        }
                        appendRow(sb, "Include Stack", stackSb.toString());
                        // also show top-of-stack (currently including file) and depth
                        appendRow(sb, "Include Depth", Integer.toString(stack.size()));
                        appendRow(sb, "Current Including File", escapeHtml(stack.peek().toString()));
                    } else {
                        appendRow(sb, "Include Stack", "(empty)");
                    }
                } catch (Exception ex) {
                    // defensive: do not let debug rendering crash the page
                    appendRow(sb, "Include Stack", "(unavailable: " + escapeHtml(ex.getMessage()) + ")");
                }

                // Optionally include helpful next-steps
                sb.append("</table>");
                sb.append("<div style=\"margin-top:8px;color:#444;font-size:12px;\">");
                sb.append(
                        "Tip: Expand this panel to see include chain and engine settings. In production set IssueHandleMode to THROW or COMMENT.");
                sb.append("</div>");
                sb.append("</div>"); // end div
                sb.append("</details>\n");

                sb.append("<!-- ISSUE DEBUG END -->\n");
            } else if (settings.issueHandleMode == IssueHandleMode.IGNORE) {
                // intentionally do nothing
            }
        }

        // small helper to append a table row (keeps HTML escaped where appropriate)
        private void appendRow(StringBuilder sb, String name, String valueHtml) {
            sb.append("<tr>");
            sb.append("<td style=\"padding:6px;border:1px solid #eee;width:22%;font-weight:bold;background:#f8f8f8;\">")
                    .append(escapeHtml(name))
                    .append("</td>");
            sb.append("<td style=\"padding:6px;border:1px solid #eee;background:#fff;\">")
                    .append(valueHtml == null ? "" : valueHtml)
                    .append("</td>");
            sb.append("</tr>");
        }

        // HTML-escape helper (very small and safe)
        private String escapeHtml(String s) {
            if (s == null)
                return "";
            StringBuilder out = new StringBuilder(Math.max(16, s.length()));
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                switch (c) {
                    case '&' -> out.append("&amp;");
                    case '<' -> out.append("&lt;");
                    case '>' -> out.append("&gt;");
                    case '"' -> out.append("&quot;");
                    case '\'' -> out.append("&#x27;");
                    default -> out.append(c);
                }
            }
            return out.toString();
        }
    }

    private final AstParser parser = new AstParser();
    private final Settings settings;
    private final FunctionLibrary functionLibrary;
    private final RuntimeIssueResolver issueResolver = new IssueResolver();

    // AST cache
    private final Map<Path, TemplateNode> astCache = new HashMap<>();

    public JhpEngine(Settings settings, FunctionLibrary functionLibrary) {
        this.settings = settings;
        this.functionLibrary = functionLibrary;
    }

    public String render(Path templatePath, Map<String, Object> context) throws Exception {
        TemplateNode ast = parser.parse(templatePath);
        AstRenderer renderer = new AstRenderer(settings, functionLibrary, issueResolver, new IncludePathResolver(),
                parser);
        return renderer.render(ast, context);
    }
}
