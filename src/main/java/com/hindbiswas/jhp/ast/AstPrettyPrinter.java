package com.hindbiswas.jhp.ast;

import java.util.*;

/**
 * Fancy AST pretty-printer that prints a tree with box-drawing branches and
 * useful short labels for each AST node.
 *
 * Usage:
 *   AstPrettyPrinter.print(astRoot);
 *   // or String s = AstPrettyPrinter.toString(astRoot);
 */
public final class AstPrettyPrinter {
    private static final String TEE = "├── ";
    private static final String ANGLE = "└── ";
    private static final String VLINE = "│   ";
    private static final String SPACE = "    ";

    private AstPrettyPrinter() {}

    public static void print(AstNode node) {
        System.out.println(toString(node));
    }

    public static String toString(AstNode node) {
        StringBuilder sb = new StringBuilder();
        build(node, sb, "", true);
        return sb.toString();
    }

    private static void build(Object node, StringBuilder sb, String prefix, boolean isRoot) {
        if (node == null) {
            sb.append(prefix).append(labelNull()).append('\n');
            return;
        }

        String label = nodeLabel(node);

        if (isRoot) {
            sb.append(label).append('\n');
        } else {
            sb.append(prefix).append(label).append('\n');
        }

        List<Object> children = childrenOf(node);
        for (int i = 0; i < children.size(); i++) {
            Object child = children.get(i);
            boolean last = (i == children.size() - 1);
            String childPrefix = prefix + (isRoot ? "" : (last ? SPACE : VLINE));
            sb.append(prefix); // prefix already printed on previous line for non-root? keep branching consistent
            // But to draw connectors we print differently: show connector then child content
            // buildConnectorLine handles connector printing and recursive build call
            buildConnectorLine(child, sb, prefix, last);
        }
    }

    // Print connector and recursively build child
    private static void buildConnectorLine(Object child, StringBuilder sb, String parentPrefix, boolean isLast) {
        String connector = isLast ? ANGLE : TEE;
        // We need to construct newPrefix for next level
        String newPrefix = parentPrefix + (isLast ? SPACE : VLINE);
        // Print connector + child label line without duplicating extra prefix
        if (child == null) {
            sb.append(connector).append(labelNull()).append('\n');
            return;
        }
        sb.append(connector).append(nodeLabel(child)).append('\n');

        List<Object> grandChildren = childrenOf(child);
        for (int i = 0; i < grandChildren.size(); i++) {
            Object gc = grandChildren.get(i);
            boolean lastGc = (i == grandChildren.size() - 1);
            // recurse with newPrefix (prefix to be used before connector)
            // but we need to append the newPrefix characters before connectors in deeper levels:
            // So for each deeper child we prepend newPrefix to the line. We'll achieve that by inserting it into sb
            // Build subtree into a temp StringBuilder then indent its lines with newPrefix
            StringBuilder sub = new StringBuilder();
            buildConnectorLineRecursive(gc, sub, newPrefix, lastGc);
            // append sub (already has proper newPrefix prepended on each line)
            sb.append(sub);
        }
    }

    // Helper recursive method which writes lines already prefixed appropriately.
    private static void buildConnectorLineRecursive(Object node, StringBuilder sb, String prefix, boolean isLast) {
        String connector = isLast ? ANGLE : TEE;
        if (node == null) {
            sb.append(prefix).append(connector).append(labelNull()).append('\n');
            return;
        }
        sb.append(prefix).append(connector).append(nodeLabel(node)).append('\n');
        List<Object> children = childrenOf(node);
        String childPrefix = prefix + (isLast ? SPACE : VLINE);
        for (int i = 0; i < children.size(); i++) {
            Object child = children.get(i);
            boolean last = (i == children.size() - 1);
            buildConnectorLineRecursive(child, sb, childPrefix, last);
        }
    }

    // Human-friendly single-line label for a node
    private static String nodeLabel(Object node) {
        if (node == null) return labelNull();

        // Template elements
        if (node instanceof TemplateNode) {
            TemplateNode t = (TemplateNode) node;
            return "Template";
        }
        if (node instanceof TextNode) {
            TextNode txt = (TextNode) node;
            return "Text " + q(ellipsize(escapeNewlines(txt.text), 60));
        }
        if (node instanceof EchoNode) {
            return "Echo";
        }
        if (node instanceof RawEchoNode) {
            return "RawEcho";
        }
        if (node instanceof IfNode) {
            return "If";
        }
        if (node instanceof ElseIfPart) {
            return "ElseIf";
        }
        if (node instanceof ForNode) {
            ForNode f = (ForNode) node;
            String upd = f.updateOp != null ? f.updateOp : (f.updateAssignExpr != null ? ("= " + shortExpr(f.updateAssignExpr)) : "");
            return "For init=" + f.initIdentifier + " update=" + (upd.isEmpty() ? "-" : upd);
        }
        if (node instanceof ForeachNode) {
            ForeachNode fn = (ForeachNode) node;
            return "Foreach " + (fn.keyIdentifier != null ? (fn.keyIdentifier + ",") : "") + fn.valueIdentifier + " in";
        }
        if (node instanceof WhileNode) {
            return "While";
        }
        if (node instanceof BreakNode) return "Break";
        if (node instanceof ContinueNode) return "Continue";
        if (node instanceof IncludeNode) {
            IncludeNode inc = (IncludeNode) node;
            return "Include " + q(inc.path);
        }

        // Expression nodes
        if (node instanceof LiteralNode) {
            LiteralNode lit = (LiteralNode) node;
            return "Literal " + q(ellipsize(String.valueOf(lit.value), 60));
        }
        if (node instanceof IdentifierNode) {
            IdentifierNode id = (IdentifierNode) node;
            return "Ident " + id.name;
        }
        if (node instanceof MemberAccessNode) {
            MemberAccessNode m = (MemberAccessNode) node;
            return "Member ." + m.member;
        }
        if (node instanceof ArrayAccessNode) {
            return "Index";
        }
        if (node instanceof FunctionCallNode) {
            FunctionCallNode c = (FunctionCallNode) node;
            return "Call " + shortExpr(c.callee) + "(" + c.args.size() + " args)";
        }
        if (node instanceof UnaryOpNode) {
            UnaryOpNode u = (UnaryOpNode) node;
            return "Unary " + u.op;
        }
        if (node instanceof BinaryOpNode) {
            BinaryOpNode b = (BinaryOpNode) node;
            return "Binary " + b.op;
        }
        if (node instanceof TernaryNode) {
            return "Ternary ? :";
        }

        // Fallback: unknown type
        return node.getClass().getSimpleName();
    }

    // Returns short string for small expressions to print inline (identifier or literal or op)
    private static String shortExpr(Object expr) {
        if (expr == null) return "null";
        if (expr instanceof IdentifierNode) return ((IdentifierNode) expr).name;
        if (expr instanceof LiteralNode) return String.valueOf(((LiteralNode) expr).value);
        if (expr instanceof MemberAccessNode) {
            MemberAccessNode m = (MemberAccessNode) expr;
            return shortExpr(m.left) + "." + m.member;
        }
        if (expr instanceof BinaryOpNode) {
            BinaryOpNode b = (BinaryOpNode) expr;
            return shortExpr(b.left) + " " + b.op + " " + shortExpr(b.right);
        }
        if (expr instanceof FunctionCallNode) {
            FunctionCallNode c = (FunctionCallNode) expr;
            return shortExpr(c.callee) + "(...)";
        }
        return expr.getClass().getSimpleName();
    }

    private static String escapeNewlines(String s) {
        if (s == null) return "null";
        return s.replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    private static String ellipsize(String s, int max) {
        if (s == null) return "null";
        if (s.length() <= max) return s;
        return s.substring(0, max - 3) + "...";
    }

    private static String q(String s) { return "\"" + s + "\""; }
    private static String labelNull() { return "<null>"; }

    // Return ordered children for the node (used to walk the AST)
    private static List<Object> childrenOf(Object node) {
        if (node == null) return Collections.emptyList();

        List<Object> children = new ArrayList<>();

        if (node instanceof TemplateNode) {
            children.addAll(((TemplateNode) node).elements);
            return children;
        }
        if (node instanceof TextNode) return Collections.emptyList();
        if (node instanceof EchoNode) {
            children.add(((EchoNode) node).expr);
            return children;
        }
        if (node instanceof RawEchoNode) {
            children.add(((RawEchoNode) node).expr);
            return children;
        }
        if (node instanceof IfNode) {
            IfNode i = (IfNode) node;
            // cond, then-block elements, each elseif part, else-block
            children.add(i.condition);
            children.addAll(i.thenBranch);
            for (ElseIfPart e : i.elseIfs) children.add(e);
            if (!i.elseBranch.isEmpty()) children.addAll(i.elseBranch);
            return children;
        }
        if (node instanceof ElseIfPart) {
            ElseIfPart e = (ElseIfPart) node;
            children.add(e.condition);
            children.addAll(e.body);
            return children;
        }
        if (node instanceof ForNode) {
            ForNode f = (ForNode) node;
            children.add(f.initExpr);
            children.add(f.condition);
            if (f.updateAssignExpr != null) children.add(f.updateAssignExpr);
            children.addAll(f.body);
            return children;
        }
        if (node instanceof ForeachNode) {
            ForeachNode f = (ForeachNode) node;
            children.add(f.iterable);
            children.addAll(f.body);
            return children;
        }
        if (node instanceof WhileNode) {
            WhileNode w = (WhileNode) node;
            children.add(w.condition);
            children.addAll(w.body);
            return children;
        }
        if (node instanceof IncludeNode) return Collections.emptyList();
        if (node instanceof BreakNode || node instanceof ContinueNode) return Collections.emptyList();

        // Expressions
        if (node instanceof LiteralNode) return Collections.emptyList();
        if (node instanceof IdentifierNode) return Collections.emptyList();
        if (node instanceof MemberAccessNode) {
            MemberAccessNode m = (MemberAccessNode) node;
            children.add(m.left);
            return children;
        }
        if (node instanceof ArrayAccessNode) {
            ArrayAccessNode a = (ArrayAccessNode) node;
            children.add(a.array);
            children.add(a.index);
            return children;
        }
        if (node instanceof FunctionCallNode) {
            FunctionCallNode c = (FunctionCallNode) node;
            children.add(c.callee);
            children.addAll(c.args);
            return children;
        }
        if (node instanceof UnaryOpNode) {
            UnaryOpNode u = (UnaryOpNode) node;
            children.add(u.expr);
            return children;
        }
        if (node instanceof BinaryOpNode) {
            BinaryOpNode b = (BinaryOpNode) node;
            children.add(b.left);
            children.add(b.right);
            return children;
        }
        if (node instanceof TernaryNode) {
            TernaryNode t = (TernaryNode) node;
            children.add(t.cond);
            children.add(t.thenExpr);
            children.add(t.elseExpr);
            return children;
        }

        // Unknown node type: try to reflectively collect fields that are AstNodes or lists of AstNodes
        try {
            // naive reflection fallback - collect public fields of type Object/AstNode/List
            var fields = node.getClass().getDeclaredFields();
            for (var f : fields) {
                f.setAccessible(true);
                Object v = f.get(node);
                if (v == null) continue;
                if (v instanceof AstNode) children.add(v);
                else if (v instanceof Collection) {
                    for (Object it : (Collection<?>) v) {
                        if (it instanceof AstNode) children.add(it);
                    }
                }
            }
        } catch (Exception ignored) {}

        return children;
    }
}
