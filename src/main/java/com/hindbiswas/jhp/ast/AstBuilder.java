package com.hindbiswas.jhp.ast;

import com.hindbiswas.jhp.JhpTemplateParserBaseVisitor;
import com.hindbiswas.jhp.JhpTemplateParser.*;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;

public class AstBuilder extends JhpTemplateParserBaseVisitor<AstNode> {

    /* ---------- Top-level ---------- */

    @Override
    public TemplateNode visitTemplate(TemplateContext ctx) {
        TemplateNode root = new TemplateNode();
        for (TemplateElementContext el : ctx.templateElement()) {
            TemplateElementNode node = visitTemplateElement(el);
            if (node != null) root.add(node);
        }
        return root;
    }

    @Override
    public TemplateElementNode visitTemplateElement(TemplateElementContext ctx) {
        if (ctx.TEXT() != null) {
            return new TextNode(ctx.TEXT().getText());
        } else if (ctx.echoStatement() != null) {
            ExpressionNode expr = visitExpression(ctx.echoStatement().expression());
            return new EchoNode(expr);
        } else if (ctx.rawEchoStatement() != null) {
            ExpressionNode expr = visitExpression(ctx.rawEchoStatement().expression());
            return new RawEchoNode(expr);
        } else if (ctx.controlStatement() != null) {
            return (TemplateElementNode) visit(ctx.controlStatement());
        }
        return null;
    }

    /* ---------- Control statements ---------- */

    @Override
    public TemplateElementNode visitControlStatement(ControlStatementContext ctx) {
        if (ctx.ifStatement() != null) return visitIfStatement(ctx.ifStatement());
        if (ctx.forStatement() != null) return visitForStatement(ctx.forStatement());
        if (ctx.foreachStatement() != null) return visitForeachStatement(ctx.foreachStatement());
        if (ctx.whileStatement() != null) return visitWhileStatement(ctx.whileStatement());
        if (ctx.breakStatement() != null) return new BreakNode();
        if (ctx.continueStatement() != null) return new ContinueNode();
        if (ctx.includeStatement() != null) return visitIncludeStatement(ctx.includeStatement());
        return null;
    }

    @Override
    public IncludeNode visitIncludeStatement(IncludeStatementContext ctx) {
        String raw = ctx.STRING_LITERAL().getText();
        return new IncludeNode(stripQuotes(raw));
    }

    @Override
    public IfNode visitIfStatement(IfStatementContext ctx) {
        ExpressionNode cond = visitExpression(ctx.expression());
        IfNode ifn = new IfNode(cond);

        // Partition templateElement() entries into then-block vs else/elseif blocks.
        Set<TemplateElementContext> elseOwned = new HashSet<>();
        for (ElseIfStatementContext e : ctx.elseIfStatement()) elseOwned.addAll(e.templateElement());
        if (ctx.elseStatement() != null) elseOwned.addAll(ctx.elseStatement().templateElement());

        for (TemplateElementContext te : ctx.templateElement()) {
            if (!elseOwned.contains(te)) {
                ifn.thenBranch.add((TemplateElementNode) visit(te));
            }
        }

        for (ElseIfStatementContext eic : ctx.elseIfStatement()) {
            ElseIfPart part = new ElseIfPart(visitExpression(eic.expression()));
            for (TemplateElementContext te : eic.templateElement()) part.body.add((TemplateElementNode) visit(te));
            ifn.elseIfs.add(part);
        }

        if (ctx.elseStatement() != null) {
            for (TemplateElementContext te : ctx.elseStatement().templateElement())
                ifn.elseBranch.add((TemplateElementNode) visit(te));
        }
        return ifn;
    }

    @Override
    public ForNode visitForStatement(ForStatementContext ctx) {
        // forInit: IDENTIFIER ASSIGN expression
        String initId = ctx.forInit().IDENTIFIER().getText();
        ExpressionNode initExpr = visitExpression(ctx.forInit().expression());
        ExpressionNode cond = visitExpression(ctx.expression());

        // forUpdate: two forms
        String updateId = null;
        String updateOp = null;
        ExpressionNode updateAssignExpr = null;
        ForUpdateContext upd = ctx.forUpdate();
        if (upd.PLUS_PLUS() != null || upd.MINUS_MINUS() != null) {
            updateId = upd.IDENTIFIER().getText();
            updateOp = upd.PLUS_PLUS() != null ? "++" : "--";
        } else if (upd.IDENTIFIER() != null) {
            updateId = upd.IDENTIFIER().getText();
            updateAssignExpr = visitExpression(upd.expression());
        }

        ForNode fn = new ForNode(initId, initExpr, cond, updateId, updateOp, updateAssignExpr);
        for (TemplateElementContext te : ctx.templateElement()) fn.body.add((TemplateElementNode) visit(te));
        return fn;
    }

    @Override
    public ForeachNode visitForeachStatement(ForeachStatementContext ctx) {
        List<TerminalNode> ids = ctx.IDENTIFIER();
        String key = null;
        String value;
        if (ids.size() == 2) { key = ids.get(0).getText(); value = ids.get(1).getText(); }
        else value = ids.get(0).getText();

        ExpressionNode iterable = visitExpression(ctx.expression());
        ForeachNode fn = new ForeachNode(key, value, iterable);
        for (TemplateElementContext te : ctx.templateElement()) fn.body.add((TemplateElementNode) visit(te));
        return fn;
    }

    @Override
    public WhileNode visitWhileStatement(WhileStatementContext ctx) {
        ExpressionNode cond = visitExpression(ctx.expression());
        WhileNode wn = new WhileNode(cond);
        for (TemplateElementContext te : ctx.templateElement()) wn.body.add((TemplateElementNode) visit(te));
        return wn;
    }

    /* ---------- Expression helper ---------- */
    public ExpressionNode visitExpression(ExpressionContext ctx) {
        if (ctx == null) return null;
        AstNode res = (AstNode) visit(ctx); // dispatch to labeled visit methods
        return (ExpressionNode) res;
    }

    /* ---------- Labeled alternative visitors ---------- */

    @Override
    public ExpressionNode visitPrimaryExpression(PrimaryExpressionContext ctx) {
        return visitPrimary(ctx.primary());
    }

    @Override
    public ExpressionNode visitArrayAccessExpression(ArrayAccessExpressionContext ctx) {
        ExpressionNode left = visitExpression(ctx.expression(0));
        ExpressionNode idx = visitExpression(ctx.expression(1));
        return new ArrayAccessNode(left, idx);
    }

    @Override
    public ExpressionNode visitMemberAccessExpression(MemberAccessExpressionContext ctx) {
        ExpressionNode left = visitExpression(ctx.expression());
        String member = ctx.IDENTIFIER() != null ? ctx.IDENTIFIER().getText()
                : (ctx.EXPR_IDENTIFIER() != null ? ctx.EXPR_IDENTIFIER().getText() : ctx.getChild(2).getText());
        return new MemberAccessNode(left, member);
    }

    @Override
    public ExpressionNode visitFunctionCallExpression(FunctionCallExpressionContext ctx) {
        return visitFunctionCall(ctx.functionCall());
    }

    @Override
    public ExpressionNode visitParenthesizedExpression(ParenthesizedExpressionContext ctx) {
        return visitExpression(ctx.expression());
    }

    @Override
    public ExpressionNode visitNotExpression(NotExpressionContext ctx) {
        String op = ctx.getChild(0).getText();
        ExpressionNode rhs = visitExpression(ctx.expression());
        return new UnaryOpNode(op, rhs);
    }

    @Override
    public ExpressionNode visitUnaryMinusExpression(UnaryMinusExpressionContext ctx) {
        String op = ctx.getChild(0).getText();
        ExpressionNode rhs = visitExpression(ctx.expression());
        return new UnaryOpNode(op, rhs);
    }

    @Override
    public ExpressionNode visitMultiplicativeExpression(MultiplicativeExpressionContext ctx) {
        ExpressionNode left = visitExpression(ctx.expression(0));
        ExpressionNode right = visitExpression(ctx.expression(1));
        String op = ctx.op.getText();
        return new BinaryOpNode(left, op, right);
    }

    @Override
    public ExpressionNode visitAdditiveExpression(AdditiveExpressionContext ctx) {
        ExpressionNode left = visitExpression(ctx.expression(0));
        ExpressionNode right = visitExpression(ctx.expression(1));
        String op = ctx.op.getText();
        return new BinaryOpNode(left, op, right);
    }

    @Override
    public ExpressionNode visitRelationalExpression(RelationalExpressionContext ctx) {
        ExpressionNode left = visitExpression(ctx.expression(0));
        ExpressionNode right = visitExpression(ctx.expression(1));
        String op = ctx.op.getText();
        return new BinaryOpNode(left, op, right);
    }

    @Override
    public ExpressionNode visitEqualityExpression(EqualityExpressionContext ctx) {
        ExpressionNode left = visitExpression(ctx.expression(0));
        ExpressionNode right = visitExpression(ctx.expression(1));
        String op = ctx.op.getText();
        return new BinaryOpNode(left, op, right);
    }

    @Override
    public ExpressionNode visitAndExpression(AndExpressionContext ctx) {
        ExpressionNode left = visitExpression(ctx.expression(0));
        ExpressionNode right = visitExpression(ctx.expression(1));
        String op = ctx.getChild(1).getText();
        return new BinaryOpNode(left, op, right);
    }

    @Override
    public ExpressionNode visitOrExpression(OrExpressionContext ctx) {
        ExpressionNode left = visitExpression(ctx.expression(0));
        ExpressionNode right = visitExpression(ctx.expression(1));
        String op = ctx.getChild(1).getText();
        return new BinaryOpNode(left, op, right);
    }

    @Override
    public ExpressionNode visitTernaryExpression(TernaryExpressionContext ctx) {
        ExpressionNode cond = visitExpression(ctx.expression(0));
        ExpressionNode thenExpr = visitExpression(ctx.expression(1));
        ExpressionNode elseExpr = visitExpression(ctx.expression(2));
        return new TernaryNode(cond, thenExpr, elseExpr);
    }

    /* ---------- Primary / function call ---------- */

    @Override
    public ExpressionNode visitPrimary(PrimaryContext ctx) {
        if (ctx.IDENTIFIER() != null) return new IdentifierNode(ctx.IDENTIFIER().getText());
        if (ctx.NUMBER() != null) {
            String t = ctx.NUMBER().getText();
            if (t.contains(".")) return new LiteralNode(Double.parseDouble(t));
            return new LiteralNode(Long.parseLong(t));
        }
        if (ctx.STRING_LITERAL() != null) return new LiteralNode(stripQuotes(ctx.STRING_LITERAL().getText()));
        if (ctx.BOOLEAN_LITERAL() != null) return new LiteralNode(Boolean.parseBoolean(ctx.BOOLEAN_LITERAL().getText()));
        if (ctx.NULL() != null) return new LiteralNode(null);

        if (ctx.EXPR_IDENTIFIER() != null) return new IdentifierNode(ctx.EXPR_IDENTIFIER().getText());
        if (ctx.EXPR_NUMBER() != null) {
            String t = ctx.EXPR_NUMBER().getText();
            if (t.contains(".")) return new LiteralNode(Double.parseDouble(t));
            return new LiteralNode(Long.parseLong(t));
        }
        if (ctx.EXPR_STRING_LITERAL() != null) return new LiteralNode(stripQuotes(ctx.EXPR_STRING_LITERAL().getText()));
        if (ctx.EXPR_BOOLEAN_LITERAL() != null) return new LiteralNode(Boolean.parseBoolean(ctx.EXPR_BOOLEAN_LITERAL().getText()));
        if (ctx.EXPR_NULL() != null) return new LiteralNode(null);

        return new LiteralNode(null);
    }

    @Override
    public ExpressionNode visitFunctionCall(FunctionCallContext ctx) {
        ExpressionNode callee;
        if (ctx.IDENTIFIER() != null) callee = new IdentifierNode(ctx.IDENTIFIER().getText());
        else if (ctx.EXPR_IDENTIFIER() != null) callee = new IdentifierNode(ctx.EXPR_IDENTIFIER().getText());
        else callee = new IdentifierNode(ctx.getChild(0).getText());

        FunctionCallNode call = new FunctionCallNode(callee);
        if (ctx.argumentList() != null) {
            for (ExpressionContext e : ctx.argumentList().expression()) call.args.add(visitExpression(e));
        }
        return call;
    }

    /* ---------- Helpers ---------- */

    private static String stripQuotes(String s) {
        if (s == null) return null;
        if (s.length() >= 2 && ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'")))) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }
}