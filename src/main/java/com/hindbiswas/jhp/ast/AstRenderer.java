package com.hindbiswas.jhp.ast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.tree.ParseTree;

import com.hindbiswas.jhp.JhpTemplateLexer;
import com.hindbiswas.jhp.JhpTemplateParser;

public class AstRenderer {
    private final ThreadLocal<Deque<Path>> includeStack = ThreadLocal.withInitial(ArrayDeque::new);
    private final Map<Path, TemplateNode> astCache = new HashMap<>();
    private final Path baseDir; // used for includes, nullable

    // control flow signals for loops
    private static class ControlFlow extends RuntimeException {
        final String tag; // "break" or "continue"

        ControlFlow(String tag) {
            this.tag = tag;
        }
    }

    public AstRenderer(Path baseDir) {
        this.baseDir = baseDir;
    }

    public AstRenderer() {
        this(null);
    }

    private TemplateNode parseAndBuild(Path path) throws Exception {
        Path normalized = path.toAbsolutePath().normalize();
        synchronized (astCache) {
            TemplateNode cached = astCache.get(normalized);
            if (cached != null)
                return cached;
        }
        // read and parse
        String text = Files.readString(normalized);
        // create parser (reuse code from your App/ParseTreePrinter):
        CharStream cs = CharStreams.fromString(text);
        JhpTemplateLexer lexer = new JhpTemplateLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JhpTemplateParser parser = new JhpTemplateParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new DiagnosticErrorListener());
        ParseTree tree = parser.template();

        AstBuilder builder = new AstBuilder();
        TemplateNode ast = (TemplateNode) builder.visit(tree);

        synchronized (astCache) {
            astCache.put(normalized, ast);
        }
        return ast;
    }

    // helper: resolve include path (returns null if not allowed/found depending on
    // mode)
    private Path resolveIncludePath(String includeExprText, Path includingFileDir) {
        String raw = includeExprText;
        // optionally strip quotes if you pass literal string; or eval expression
        // earlier
        Path p = Path.of(raw);
        if (!p.isAbsolute()) {
            if (includingFileDir != null)
                p = includingFileDir.resolve(p);
            else if (baseDir != null)
                p = baseDir.resolve(p);
        }
        p = p.normalize();

        // ensure jhp extension: optionally append .jhp if missing
        if (!p.getFileName().toString().endsWith(".jhp")) {
            p = p.resolveSibling(p.getFileName().toString() + ".jhp");
        }

        // security: ensure inside baseDir if baseDir is set
        if (baseDir != null) {
            Path baseNorm = baseDir.toAbsolutePath().normalize();
            if (!p.toAbsolutePath().normalize().startsWith(baseNorm)) {
                // disallow escaping baseDir
                return null;
            }
        }
        return p;
    }

    public String render(TemplateNode root, Map<String, Object> context) {
        Deque<Path> stack = includeStack.get();
        stack.clear();

        // use a stack of scopes (for loop variables)
        Deque<Map<String, Object>> scopes = new ArrayDeque<>();
        scopes.push(new HashMap<>(context != null ? context : Map.of()));
        StringBuilder sb = new StringBuilder();
        renderElements(root.elements, scopes, sb);
        return sb.toString();
    }

    private void renderElements(List<TemplateElementNode> elements, Deque<Map<String, Object>> scopes,
            StringBuilder sb) {
        for (TemplateElementNode el : elements) {
            renderElement(el, scopes, sb);
        }
    }

    private void renderElement(TemplateElementNode el, Deque<Map<String, Object>> scopes, StringBuilder sb) {
        if (el instanceof TextNode) {
            sb.append(((TextNode) el).text);
            return;
        }
        if (el instanceof EchoNode) {
            Object v = evalExpression(((EchoNode) el).expr, scopes);
            sb.append(escapeHtml(stringify(v)));
            return;
        }
        if (el instanceof RawEchoNode) {
            Object v = evalExpression(((RawEchoNode) el).expr, scopes);
            sb.append(stringify(v));
            return;
        }
        if (el instanceof IfNode) {
            IfNode in = (IfNode) el;
            if (toBoolean(evalExpression(in.condition, scopes))) {
                renderElements(in.thenBranch, scopes, sb);
                return;
            }
            for (ElseIfPart e : in.elseIfs) {
                if (toBoolean(evalExpression(e.condition, scopes))) {
                    renderElements(e.body, scopes, sb);
                    return;
                }
            }
            if (!in.elseBranch.isEmpty()) {
                renderElements(in.elseBranch, scopes, sb);
            }
            return;
        }
        if (el instanceof ForNode) {
            ForNode fn = (ForNode) el;
            // create local scope for loop variable
            Map<String, Object> local = new HashMap<>();
            scopes.push(local);
            try {
                // init
                if (fn.initIdentifier != null && fn.initExpr != null) {
                    Object initVal = evalExpression(fn.initExpr, scopes);
                    local.put(fn.initIdentifier, initVal);
                }
                // loop
                while (true) {
                    Object condVal = evalExpression(fn.condition, scopes);
                    if (!toBoolean(condVal))
                        break;
                    try {
                        renderElements(fn.body, scopes, sb);
                    } catch (ControlFlow cf) {
                        if ("break".equals(cf.tag))
                            break;
                        if ("continue".equals(cf.tag)) {
                            // continue semantics -> perform update then continue
                        }
                    }
                    // update
                    if (fn.updateOp != null) {
                        Object cur = lookup(fn.updateIdentifier, scopes);
                        Number n = toNumber(cur);
                        if (n == null) {
                            local.put(fn.updateIdentifier, 1L);
                        } else {
                            if ("++".equals(fn.updateOp))
                                local.put(fn.updateIdentifier, n.longValue() + 1);
                            else
                                local.put(fn.updateIdentifier, n.longValue() - 1);
                        }
                    } else if (fn.updateAssignExpr != null) {
                        Object newv = evalExpression(fn.updateAssignExpr, scopes);
                        setIdentifier(fn.updateIdentifier, newv, scopes);
                    }
                }
            } finally {
                scopes.pop();
            }
            return;
        }
        if (el instanceof ForeachNode) {
            ForeachNode fe = (ForeachNode) el;
            Object it = evalExpression(fe.iterable, scopes);
            if (it == null)
                return;
            if (it instanceof Map) {
                Map<?, ?> m = (Map<?, ?>) it;
                for (Map.Entry<?, ?> e : m.entrySet()) {
                    Map<String, Object> local = new HashMap<>();
                    if (fe.keyIdentifier != null)
                        local.put(fe.keyIdentifier, e.getKey());
                    local.put(fe.valueIdentifier, e.getValue());
                    scopes.push(local);
                    try {
                        renderElements(fe.body, scopes, sb);
                    } catch (ControlFlow cf) {
                        if ("break".equals(cf.tag)) {
                            scopes.pop();
                            break;
                        }
                        // continue -> just pop and continue
                    } finally {
                        if (!scopes.isEmpty() && scopes.peek() == local)
                            scopes.pop();
                    }
                }
            } else if (it instanceof Iterable) {
                Iterable<?> itr = (Iterable<?>) it;
                for (Object item : itr) {
                    Map<String, Object> local = new HashMap<>();
                    local.put(fe.valueIdentifier, item);
                    if (fe.keyIdentifier != null)
                        local.put(fe.keyIdentifier, null);
                    scopes.push(local);
                    try {
                        renderElements(fe.body, scopes, sb);
                    } catch (ControlFlow cf) {
                        if ("break".equals(cf.tag)) {
                            scopes.pop();
                            break;
                        }
                    } finally {
                        if (!scopes.isEmpty() && scopes.peek() == local)
                            scopes.pop();
                    }
                }
            } else if (it.getClass().isArray()) {
                int len = java.lang.reflect.Array.getLength(it);
                for (int i = 0; i < len; i++) {
                    Object item = java.lang.reflect.Array.get(it, i);
                    Map<String, Object> local = new HashMap<>();
                    if (fe.keyIdentifier != null)
                        local.put(fe.keyIdentifier, i);
                    local.put(fe.valueIdentifier, item);
                    scopes.push(local);
                    try {
                        renderElements(fe.body, scopes, sb);
                    } catch (ControlFlow cf) {
                        if ("break".equals(cf.tag)) {
                            scopes.pop();
                            break;
                        }
                    } finally {
                        if (!scopes.isEmpty() && scopes.peek() == local)
                            scopes.pop();
                    }
                }
            } else {
                // not iterable: nothing
            }
            return;
        }
        if (el instanceof WhileNode) {
            WhileNode wn = (WhileNode) el;
            while (toBoolean(evalExpression(wn.condition, scopes))) {
                try {
                    renderElements(wn.body, scopes, sb);
                } catch (ControlFlow cf) {
                    if ("break".equals(cf.tag))
                        break;
                    if ("continue".equals(cf.tag))
                        continue;
                }
            }
            return;
        }
        if (el instanceof BreakNode) {
            throw new ControlFlow("break");
        }
        if (el instanceof ContinueNode) {
            throw new ControlFlow("continue");
        }
        if (el instanceof IncludeNode) {
            IncludeNode inc = (IncludeNode) el;
            Deque<Path> stack = includeStack.get();
            Path includingFileDir = stack.isEmpty() ? baseDir : stack.peek().getParent();
            Path resolved = resolveIncludePath(inc.path, includingFileDir);
            if (resolved == null) {
                sb.append("<!-- include disallowed or not found: ").append(inc.path).append(" -->");
                return;
            }

            // normalize
            resolved = resolved.toAbsolutePath().normalize();

            // cycle detection
            if (stack.contains(resolved)) {
                StringBuilder cyc = new StringBuilder();
                for (Path p : stack) {
                    cyc.append(p.toString()).append(" -> ");
                }
                cyc.append(resolved.toString());
                sb.append("<!-- include cycle detected: ").append(cyc).append(" -->");
                return;
            }

            try {
                TemplateNode includedAst = parseAndBuild(resolved);
                stack.push(resolved);
                try {
                    // render included template in a new local scope
                    Map<String, Object> local = new HashMap<>();
                    scopes.push(local);
                    try {
                        renderElements(includedAst.elements, scopes, sb);
                    } finally {
                        // pop scope
                        if (!scopes.isEmpty() && scopes.peek() == local)
                            scopes.pop();
                    }
                } finally {
                    // pop include stack entry
                    Path popped = stack.pop();
                    assert popped.equals(resolved);
                }
            } catch (Exception ex) {
                sb.append("<!-- include error: ").append(ex.getClass().getSimpleName())
                        .append(": ").append(ex.getMessage()).append(" -->");
            }
            return;
        }

        // Unknown element: ignore
    }

    /* ---------------- Expression evaluation ---------------- */

    private Object evalExpression(ExpressionNode expr, Deque<Map<String, Object>> scopes) {
        if (expr == null)
            return null;

        if (expr instanceof LiteralNode) {
            return ((LiteralNode) expr).value;
        }
        if (expr instanceof IdentifierNode) {
            return lookup(((IdentifierNode) expr).name, scopes);
        }
        if (expr instanceof MemberAccessNode) {
            MemberAccessNode m = (MemberAccessNode) expr;
            Object left = evalExpression(m.left, scopes);
            return resolveMember(left, m.member);
        }
        if (expr instanceof ArrayAccessNode) {
            ArrayAccessNode a = (ArrayAccessNode) expr;
            Object arr = evalExpression(a.array, scopes);
            Object idx = evalExpression(a.index, scopes);
            return indexAccess(arr, idx);
        }
        if (expr instanceof FunctionCallNode) {
            FunctionCallNode f = (FunctionCallNode) expr;
            Object callee = evalExpression(f.callee, scopes);
            List<Object> args = new ArrayList<>();
            for (ExpressionNode e : f.args)
                args.add(evalExpression(e, scopes));
            return callFunction(callee, args);
        }
        if (expr instanceof UnaryOpNode) {
            UnaryOpNode u = (UnaryOpNode) expr;
            Object v = evalExpression(u.expr, scopes);
            String op = u.op;
            if ("!".equals(op))
                return !toBoolean(v);
            if ("-".equals(op)) {
                Number n = toNumber(v);
                if (n == null)
                    return 0;
                if (n instanceof Double)
                    return -n.doubleValue();
                return -n.longValue();
            }
            // fallback
            return null;
        }
        if (expr instanceof BinaryOpNode) {
            BinaryOpNode b = (BinaryOpNode) expr;
            Object L = evalExpression(b.left, scopes);
            Object R = evalExpression(b.right, scopes);
            String op = b.op;
            return applyBinary(L, op, R);
        }
        if (expr instanceof TernaryNode) {
            TernaryNode t = (TernaryNode) expr;
            Object c = evalExpression(t.cond, scopes);
            if (toBoolean(c))
                return evalExpression(t.thenExpr, scopes);
            return evalExpression(t.elseExpr, scopes);
        }

        // unknown expr
        return null;
    }

    /* --------------- supporting helpers --------------- */

    private Object lookup(String name, Deque<Map<String, Object>> scopes) {
        for (Map<String, Object> s : scopes) {
            if (s.containsKey(name))
                return s.get(name);
        }
        return null;
    }

    private void setIdentifier(String name, Object value, Deque<Map<String, Object>> scopes) {
        for (Map<String, Object> s : scopes) {
            if (s.containsKey(name)) {
                s.put(name, value);
                return;
            }
        }
        // if not present, set in topmost (current) scope
        scopes.peek().put(name, value);
    }

    private Object resolveMember(Object left, String member) {
        if (left == null)
            return null;
        if (left instanceof Map) {
            return ((Map<?, ?>) left).get(member);
        }
        // try bean getter
        try {
            String getter = "get" + Character.toUpperCase(member.charAt(0)) + member.substring(1);
            Method m = left.getClass().getMethod(getter);
            return m.invoke(left);
        } catch (Exception ignored) {
        }
        try {
            Field f = left.getClass().getDeclaredField(member);
            f.setAccessible(true);
            return f.get(left);
        } catch (Exception ignored) {
        }
        return null;
    }

    private Object indexAccess(Object arr, Object idx) {
        if (arr == null || idx == null)
            return null;
        if (arr instanceof List) {
            int i = ((Number) toNumber(idx)).intValue();
            List<?> l = (List<?>) arr;
            return i >= 0 && i < l.size() ? l.get(i) : null;
        }
        if (arr.getClass().isArray()) {
            int i = ((Number) toNumber(idx)).intValue();
            return java.lang.reflect.Array.get(arr, i);
        }
        if (arr instanceof Map) {
            return ((Map<?, ?>) arr).get(idx);
        }
        return null;
    }

    private Object callFunction(Object callee, List<Object> args) {
        // Simple built-in functions impl (temporary)
        if (callee instanceof String) {
            String name = (String) callee;
            if ("len".equals(name) && args.size() == 1) {
                Object a = args.get(0);
                if (a instanceof String)
                    return ((String) a).length();
                if (a instanceof Collection)
                    return ((Collection<?>) a).size();
                if (a instanceof Map)
                    return ((Map<?, ?>) a).size();
                if (a != null && a.getClass().isArray())
                    return java.lang.reflect.Array.getLength(a);
                return 0;
            }
        }
        // fallback: return null
        return null;
    }

    private String stringify(Object o) {
        if (o == null)
            return "";
        return o.toString();
    }

    private String escapeHtml(String s) {
        if (s == null)
            return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#x27;");
    }

    private boolean toBoolean(Object o) {
        if (o == null)
            return false;
        if (o instanceof Boolean)
            return (Boolean) o;
        if (o instanceof Number)
            return ((Number) o).doubleValue() != 0.0;
        String s = o.toString().trim().toLowerCase();
        return !(s.isEmpty() || "false".equals(s) || "0".equals(s) || "null".equals(s));
    }

    private Number toNumber(Object o) {
        if (o == null)
            return null;
        if (o instanceof Number)
            return (Number) o;
        try {
            if (o.toString().contains("."))
                return Double.parseDouble(o.toString());
            return Long.parseLong(o.toString());
        } catch (Exception ex) {
            return null;
        }
    }

    private Object applyBinary(Object L, String op, Object R) {
        // arithmetic and concat
        if ("+".equals(op)) {
            if (L instanceof Number && R instanceof Number) {
                Number a = toNumber(L), b = toNumber(R);
                if (a instanceof Double || b instanceof Double)
                    return a.doubleValue() + b.doubleValue();
                return a.longValue() + b.longValue();
            }
            return stringify(L) + stringify(R);
        }
        if ("-".equals(op)) {
            Number a = toNumber(L), b = toNumber(R);
            return a.doubleValue() - b.doubleValue();
        }
        if ("*".equals(op)) {
            Number a = toNumber(L), b = toNumber(R);
            return a.doubleValue() * b.doubleValue();
        }
        if ("/".equals(op)) {
            Number a = toNumber(L), b = toNumber(R);
            return a.doubleValue() / b.doubleValue();
        }
        if ("%".equals(op)) {
            Number a = toNumber(L), b = toNumber(R);
            return a.doubleValue() % b.doubleValue();
        }

        // comparisons
        if (">".equals(op))
            return toNumber(L).doubleValue() > toNumber(R).doubleValue();
        if ("<".equals(op))
            return toNumber(L).doubleValue() < toNumber(R).doubleValue();
        if (">=".equals(op))
            return toNumber(L).doubleValue() >= toNumber(R).doubleValue();
        if ("<=".equals(op))
            return toNumber(L).doubleValue() <= toNumber(R).doubleValue();
        if ("==".equals(op) || "=".equals(op))
            return Objects.equals(L, R);
        if ("!=".equals(op))
            return !Objects.equals(L, R);

        // logical
        if ("&&".equals(op))
            return toBoolean(L) && toBoolean(R);
        if ("||".equals(op))
            return toBoolean(L) || toBoolean(R);

        return null;
    }
}
