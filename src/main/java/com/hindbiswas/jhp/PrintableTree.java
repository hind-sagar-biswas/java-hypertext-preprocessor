package com.hindbiswas.jhp;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.hindbiswas.jhp.JhpTemplateParser.PrimaryExpressionContext;

public class PrintableTree {
    public final String ANGLE = "└──";
    public final String TEE = "├──";
    public final String LINE = "──";

    public ParseTree tree;

    public PrintableTree() {
    }

    public PrintableTree(ParseTree tree) {
        this.tree = tree;
    }

    public PrintableTree setTree(ParseTree tree) {
        this.tree = tree;
        return this;
    }

    private String treeString(ParseTree node, String prefix) {
        if (node instanceof PrimaryExpressionContext && node.getChildCount() == 0) {
            return visitPrimary((PrimaryExpressionContext) node);
        }

        if (node instanceof TerminalNode) {
            return visitTerminal((TerminalNode) node);
        }

        if (!(node instanceof RuleNode)) {
            return "";
        }

        // Get Rule name from the Parser
        int ruleIndex = ((RuleNode) node).getRuleContext().getRuleIndex();
        String name = JhpTemplateParser.ruleNames[ruleIndex];

        StringBuilder sb = new StringBuilder(name);

        for (int i = 0; i < node.getChildCount(); i++) {
            boolean isAtEnd = i == node.getChildCount() - 1;
            String symbol = isAtEnd ? ANGLE : TEE;
            String childSymbol = isAtEnd ? "   " : "│  ";

            String childStr = treeString(node.getChild(i), prefix + childSymbol + " ");

            sb.append("\n").append(prefix).append(symbol).append(" ").append(childStr);
        }

        return sb.toString();
    }

    private String visitPrimary(PrimaryExpressionContext node) {
        String name = JhpTemplateParser.ruleNames[node.getRuleContext().getRuleIndex()];
        // guard that node has at least one child and it's a TerminalNode
        if (node.getChildCount() > 0 && node.getChild(0) instanceof TerminalNode) {
            String childStr = visitTerminal((TerminalNode) node.getChild(0));
            return name + " " + LINE + " " + childStr;
        } else {
            return name;
        }
    }

    private String visitTerminal(TerminalNode node) {
        Token tok = node.getSymbol();
        int ttype = tok.getType();

        // Handle EOF explicitly
        if (ttype == Token.EOF) {
            return "EOF";
        }

        // Use the VOCABULARY to get the best available name for the token
        String symbolic = JhpTemplateLexer.VOCABULARY.getSymbolicName(ttype);
        String literal = JhpTemplateLexer.VOCABULARY.getLiteralName(ttype);
        String display = JhpTemplateLexer.VOCABULARY.getDisplayName(ttype);

        char id;
        if (symbolic != null && symbolic.startsWith("T__")) {
            id = 'P'; // original behavior: T__ -> 'P'
        } else if (symbolic != null && symbolic.length() > 0) {
            id = symbolic.charAt(0);
        } else if (literal != null && literal.length() > 0) {
            // literal looks like "'+'" — pick first non-quote char if possible
            int first = 0;
            while (first < literal.length() && literal.charAt(first) == '\'')
                first++;
            id = (first < literal.length()) ? literal.charAt(first) : 'L';
        } else {
            id = display.length() > 0 ? display.charAt(0) : '?';
        }

        // Use the token text for the visible content
        String text = tok.getText();
        return id + "'" + text + "'";
    }

    @Override
    public String toString() {
        return treeString(tree, " ");
    }
}
