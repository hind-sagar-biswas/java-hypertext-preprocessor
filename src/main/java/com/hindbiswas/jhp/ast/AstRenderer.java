package com.hindbiswas.jhp.ast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;

import com.hindbiswas.jhp.engine.FunctionLibraryContext;
import com.hindbiswas.jhp.engine.IssueType;
import com.hindbiswas.jhp.engine.PathResolver;
import com.hindbiswas.jhp.engine.PathToAstParser;
import com.hindbiswas.jhp.engine.RuntimeIssueResolver;
import com.hindbiswas.jhp.engine.Settings;
import com.hindbiswas.jhp.errors.InvalidFileTypeException;
import com.hindbiswas.jhp.errors.PathNotInBaseDirectoryException;

public class AstRenderer {
    private final Settings settings;
    private final PathToAstParser parser;
    private final PathResolver pathResolver;
    private final FunctionLibraryContext functions;
    private final RuntimeIssueResolver issueResolver;

    // per-thread include stack for cycle detection
    private final ThreadLocal<Deque<Path>> includeStack = ThreadLocal.withInitial(ArrayDeque::new);

    public AstRenderer(Settings settings, FunctionLibraryContext functions, RuntimeIssueResolver issueResolver,
            PathResolver pathResolver, PathToAstParser parser) {
        this.settings = settings;
        this.functions = functions;
        this.issueResolver = issueResolver;
        this.pathResolver = pathResolver;
        this.parser = parser;
    }

    // control flow signals for loops
    private static class ControlFlow extends RuntimeException {
        final String tag; // "break" or "continue"

        ControlFlow(String tag) {
            this.tag = tag;
        }
    }

    public String render(TemplateNode root, Map<String, Object> context) {
        Deque<Path> stack = includeStack.get();
        stack.clear();

        // stack of scopes for variables (loops, includes)
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
        switch (el.getClass().getSimpleName()) {
            case "TextNode" -> sb.append(((TextNode) el).text);
            case "EchoNode" -> renderEcho((EchoNode) el, scopes, sb, settings.escapeByDefault);
            case "RawEchoNode" -> renderEcho((RawEchoNode) el, scopes, sb);
            case "IncludeNode" -> renderInclude((IncludeNode) el, scopes, sb);
            case "IfNode" -> renderIf((IfNode) el, scopes, sb);
            case "ForNode" -> renderFor((ForNode) el, scopes, sb);
            case "ForeachNode" -> renderForeach((ForeachNode) el, scopes, sb);
            case "WhileNode" -> renderWhile((WhileNode) el, scopes, sb);
            case "BreakNode" -> handleControlFlow("break");
            case "ContinueNode" -> handleControlFlow("continue");
            default -> issueResolver.handle(IssueType.UNKNOWN_ELEMENT, "Unknown element: " + el, sb, includeStack);
        }
    }

    private void renderEcho(EchoNode el, Deque<Map<String, Object>> scopes, StringBuilder sb, boolean escape) {
        Object v = evalExpression(el.expr, scopes);
        sb.append(escape ? escapeHtml(stringify(v)) : stringify(v));
    }

    private void renderEcho(RawEchoNode el, Deque<Map<String, Object>> scopes, StringBuilder sb) {
        Object v = evalExpression(el.expr, scopes);
        sb.append(stringify(v));
    }

    private void renderInclude(IncludeNode node, Deque<Map<String, Object>> scopes, StringBuilder sb) {
        Deque<Path> stack = includeStack.get();
        Path includingFileDir = stack.isEmpty() ? settings.base : stack.peek().getParent();

        // Resolve the include path
        Path resolved;
        try {
            resolved = pathResolver.resolve(node.path, includingFileDir);

        } catch (PathNotInBaseDirectoryException e) {
            issueResolver.handle(IssueType.INCLUDE_NOT_IN_BASE_DIR, e.getMessage(), sb, includeStack);
            return;
        } catch (InvalidFileTypeException e) {
            issueResolver.handle(IssueType.MISSING_INCLUDE, e.getMessage(), sb, includeStack);
            return;
        } catch (Exception e) {
            issueResolver.handle(IssueType.INCLUDE_ERROR,
                    "Something went wrong trying to resolve the include path: " + node.path + ".", sb, includeStack);
            return;
        }

        // Check max depth
        if (stack.size() >= settings.maxIncludeDepth) {
            issueResolver.handle(IssueType.INCLUDE_MAX_DEPTH, "Max include depth reached: " + settings.maxIncludeDepth,
                    sb, includeStack);
            return;
        }

        // Cycle detection
        if (stack.contains(resolved)) {
            issueResolver.handle(IssueType.INCLUDE_CYCLE, "Include cycle detected: " + resolved, sb, includeStack);
            return;
        }

        try {
            TemplateNode includedAst = parser.parse(resolved);

            stack.push(resolved);
            try {
                // create new local scope for included template
                Map<String, Object> localScope = new HashMap<>();
                scopes.push(localScope);
                try {
                    renderElements(includedAst.elements, scopes, sb);
                } finally {
                    scopes.pop();
                }
            } finally {
                stack.pop();
            }

        } catch (Exception ex) {
            issueResolver.handle(IssueType.INCLUDE_ERROR,
                    "Something went wrong trying to parse the include: " + node.path + ".", sb, includeStack);
        }
    }

    private void renderIf(IfNode node, Deque<Map<String, Object>> scopes, StringBuilder sb) {
        if (toBoolean(evalExpression(node.condition, scopes))) {
            renderElements(node.thenBranch, scopes, sb);
            return;
        }
        for (ElseIfPart e : node.elseIfs) {
            if (toBoolean(evalExpression(e.condition, scopes))) {
                renderElements(e.body, scopes, sb);
                return;
            }
        }
        if (!node.elseBranch.isEmpty()) {
            renderElements(node.elseBranch, scopes, sb);
        }
        return;
    }

    private void renderFor(ForNode node, Deque<Map<String, Object>> scopes, StringBuilder sb) {
        Map<String, Object> local = new HashMap<>();
        scopes.push(local);
        try {
            // init
            if (node.initIdentifier != null && node.initExpr != null) {
                Object initVal = evalExpression(node.initExpr, scopes);
                local.put(node.initIdentifier, initVal);
            }
            // loop
            while (true) {
                Object condVal = evalExpression(node.condition, scopes);
                if (!toBoolean(condVal))
                    break;
                try {
                    renderElements(node.body, scopes, sb);
                } catch (ControlFlow cf) {
                    if ("break".equals(cf.tag))
                        break;
                    if ("continue".equals(cf.tag)) {
                        // continue semantics -> perform update then continue
                    }
                }
                // update
                if (node.updateOp != null) {
                    Object cur = lookup(node.updateIdentifier, scopes);
                    Number n = toNumber(cur);
                    if (n == null) {
                        local.put(node.updateIdentifier, 1L);
                    } else {
                        if ("++".equals(node.updateOp))
                            local.put(node.updateIdentifier, n.longValue() + 1);
                        else
                            local.put(node.updateIdentifier, n.longValue() - 1);
                    }
                } else if (node.updateAssignExpr != null) {
                    Object newv = evalExpression(node.updateAssignExpr, scopes);
                    setIdentifier(node.updateIdentifier, newv, scopes);
                }
            }
        } finally {
            scopes.pop();
        }
        return;
    }

    private void renderForeach(ForeachNode node, Deque<Map<String, Object>> scopes, StringBuilder sb) {
        Object it = evalExpression(node.iterable, scopes);
        if (it == null)
            return;
        if (it instanceof Map) {
            Map<?, ?> m = (Map<?, ?>) it;
            for (Map.Entry<?, ?> e : m.entrySet()) {
                Map<String, Object> local = new HashMap<>();
                if (node.keyIdentifier != null)
                    local.put(node.keyIdentifier, e.getKey());
                local.put(node.valueIdentifier, e.getValue());
                scopes.push(local);
                try {
                    renderElements(node.body, scopes, sb);
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
                local.put(node.valueIdentifier, item);
                if (node.keyIdentifier != null)
                    local.put(node.keyIdentifier, null);
                scopes.push(local);
                try {
                    renderElements(node.body, scopes, sb);
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
                if (node.keyIdentifier != null)
                    local.put(node.keyIdentifier, i);
                local.put(node.valueIdentifier, item);
                scopes.push(local);
                try {
                    renderElements(node.body, scopes, sb);
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

    private void renderWhile(WhileNode node, Deque<Map<String, Object>> scopes, StringBuilder sb) {
        while (toBoolean(evalExpression(node.condition, scopes))) {
            try {
                renderElements(node.body, scopes, sb);
            } catch (ControlFlow cf) {
                if ("break".equals(cf.tag))
                    break;
                if ("continue".equals(cf.tag))
                    continue;
            }
        }
        return;
    }

    private void handleControlFlow(String tag) {
        throw new ControlFlow(tag);
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

            // Determine function name (prefer raw identifier names)
            String fnName = null;
            if (f.callee instanceof IdentifierNode id) {
                fnName = id.name; // function name literal
            } else {
                Object calleeVal = evalExpression(f.callee, scopes);
                if (calleeVal instanceof String)
                    fnName = (String) calleeVal;
            }

            // build args
            List<Object> args = new ArrayList<>();
            for (ExpressionNode e : f.args)
                args.add(evalExpression(e, scopes));

            if (fnName != null) {
                try {
                    return functions.callFunction(fnName, args, scopes);
                } catch (Exception ex) {
                    StringBuilder tmp = new StringBuilder();
                    issueResolver.handle(IssueType.FUNCTION_ERROR,
                            "Function '" + fnName + "' threw: " + ex.getClass().getSimpleName() + ": "
                                    + ex.getMessage(),
                            tmp, includeStack);
                    return tmp.toString();
                }
            } else {
                StringBuilder tmp = new StringBuilder();
                issueResolver.handle(IssueType.FUNCTION_CALL_ERROR,
                        "Invalid function call: " + String.valueOf(f.callee), tmp, includeStack);
                return tmp.toString();
            }
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

    private Object callFunction(Object callee, List<Object> args, Deque<Map<String, Object>> scopes) {
        if (callee instanceof String name) {
            return functions.callFunction(name, args, scopes);
        }
        // unknown callee type
        StringBuilder sb = new StringBuilder();
        issueResolver.handle(IssueType.FUNCTION_CALL_ERROR, "Invalid function call: " + callee, sb, includeStack);
        return sb.toString();
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
