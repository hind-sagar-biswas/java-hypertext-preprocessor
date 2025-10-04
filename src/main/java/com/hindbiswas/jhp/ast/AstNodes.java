package com.hindbiswas.jhp.ast;

import java.util.*;

/**
 * AST node hierarchy. Simple POJOs with toString() for quick debugging.
 */

class TextNode implements TemplateElementNode {
    public final String text;

    public TextNode(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Text(" + escape(text) + ")";
    }

    private String escape(String s) {
        return s.replace("\n", "\\n");
    }
}

class EchoNode implements TemplateElementNode {
    public final ExpressionNode expr;

    public EchoNode(ExpressionNode expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "Echo(" + expr + ")";
    }
}

class RawEchoNode implements TemplateElementNode {
    public final ExpressionNode expr;

    public RawEchoNode(ExpressionNode expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "RawEcho(" + expr + ")";
    }
}

class IfNode implements TemplateElementNode {
    public final ExpressionNode condition;
    public final List<TemplateElementNode> thenBranch = new ArrayList<>();
    public final List<ElseIfPart> elseIfs = new ArrayList<>();
    public final List<TemplateElementNode> elseBranch = new ArrayList<>();

    public IfNode(ExpressionNode condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "If(cond=" + condition + ", then=" + thenBranch + ", elseIfs=" + elseIfs + ", else=" + elseBranch + ")";
    }
}

class ElseIfPart {
    public final ExpressionNode condition;
    public final List<TemplateElementNode> body = new ArrayList<>();

    public ElseIfPart(ExpressionNode condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "ElseIf(" + condition + "," + body + ")";
    }
}

class ForNode implements TemplateElementNode {
    public final String initIdentifier;
    public final ExpressionNode initExpr;
    public final ExpressionNode condition;
    public final String updateIdentifier;
    public final String updateOp; // "++" or "--" or null
    public final ExpressionNode updateAssignExpr; // optional
    public final List<TemplateElementNode> body = new ArrayList<>();

    public ForNode(String initIdentifier, ExpressionNode initExpr, ExpressionNode condition,
            String updateIdentifier, String updateOp, ExpressionNode updateAssignExpr) {
        this.initIdentifier = initIdentifier;
        this.initExpr = initExpr;
        this.condition = condition;
        this.updateIdentifier = updateIdentifier;
        this.updateOp = updateOp;
        this.updateAssignExpr = updateAssignExpr;
    }

    @Override
    public String toString() {
        return "For(init=" + initIdentifier + "=" + initExpr + "; cond=" + condition + "; update=" + updateIdentifier
                + (updateOp != null ? updateOp : "=" + updateAssignExpr) + ", body=" + body + ")";
    }
}

class ForeachNode implements TemplateElementNode {
    public final String keyIdentifier; // maybe null if not provided
    public final String valueIdentifier;
    public final ExpressionNode iterable;
    public final List<TemplateElementNode> body = new ArrayList<>();

    public ForeachNode(String keyIdentifier, String valueIdentifier, ExpressionNode iterable) {
        this.keyIdentifier = keyIdentifier;
        this.valueIdentifier = valueIdentifier;
        this.iterable = iterable;
    }

    @Override
    public String toString() {
        return "Foreach(" + (keyIdentifier != null ? keyIdentifier + "," : "") + valueIdentifier + " in " + iterable
                + ", body=" + body + ")";
    }
}

class WhileNode implements TemplateElementNode {
    public final ExpressionNode condition;
    public final List<TemplateElementNode> body = new ArrayList<>();

    public WhileNode(ExpressionNode condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "While(" + condition + ", body=" + body + ")";
    }
}

class BreakNode implements TemplateElementNode {
    @Override
    public String toString() {
        return "Break";
    }
}

class ContinueNode implements TemplateElementNode {
    @Override
    public String toString() {
        return "Continue";
    }
}

class IncludeNode implements TemplateElementNode {
    public final String path;

    public IncludeNode(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Include(" + path + ")";
    }
}

/* Expression nodes */
interface ExpressionNode extends AstNode {
}

class LiteralNode implements ExpressionNode {
    public final Object value;

    public LiteralNode(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Literal(" + value + ")";
    }
}

class IdentifierNode implements ExpressionNode {
    public final String name;

    public IdentifierNode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Ident(" + name + ")";
    }
}

class MemberAccessNode implements ExpressionNode {
    public final ExpressionNode left;
    public final String member;

    public MemberAccessNode(ExpressionNode left, String member) {
        this.left = left;
        this.member = member;
    }

    @Override
    public String toString() {
        return "Member(" + left + "." + member + ")";
    }
}

class ArrayAccessNode implements ExpressionNode {
    public final ExpressionNode array;
    public final ExpressionNode index;

    public ArrayAccessNode(ExpressionNode array, ExpressionNode index) {
        this.array = array;
        this.index = index;
    }

    @Override
    public String toString() {
        return "Index(" + array + "[" + index + "])";
    }
}

class FunctionCallNode implements ExpressionNode {
    public final ExpressionNode callee; // can be IdentifierNode or member expression
    public final List<ExpressionNode> args = new ArrayList<>();

    public FunctionCallNode(ExpressionNode callee) {
        this.callee = callee;
    }

    @Override
    public String toString() {
        return "Call(" + callee + args + ")";
    }
}

class UnaryOpNode implements ExpressionNode {
    public final String op;
    public final ExpressionNode expr;

    public UnaryOpNode(String op, ExpressionNode expr) {
        this.op = op;
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "Unary(" + op + " " + expr + ")";
    }
}

class BinaryOpNode implements ExpressionNode {
    public final ExpressionNode left;
    public final String op;
    public final ExpressionNode right;

    public BinaryOpNode(ExpressionNode left, String op, ExpressionNode right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public String toString() {
        return "Binary(" + left + " " + op + " " + right + ")";
    }
}

class TernaryNode implements ExpressionNode {
    public final ExpressionNode cond, thenExpr, elseExpr;

    public TernaryNode(ExpressionNode cond, ExpressionNode thenExpr, ExpressionNode elseExpr) {
        this.cond = cond;
        this.thenExpr = thenExpr;
        this.elseExpr = elseExpr;
    }

    @Override
    public String toString() {
        return "Ternary(" + cond + " ? " + thenExpr + " : " + elseExpr + ")";
    }
}

class BlockNode implements TemplateElementNode {
    public final String name;
    public final List<TemplateElementNode> body = new ArrayList<>();

    public BlockNode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Block(" + name + "," + body + ")";
    }
}

class NodeSpan {
    public final int start, end;

    public NodeSpan(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "[" + start + "," + end + "]";
    }
}
