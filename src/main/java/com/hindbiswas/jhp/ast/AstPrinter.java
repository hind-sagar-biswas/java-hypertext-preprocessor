package com.hindbiswas.jhp.ast;

public class AstPrinter {
    public static void print(AstNode node) {
        print(node, 0);
    }
    private static void print(Object node, int indent) {
        if (node == null) return;
        String pad = " ".repeat(indent*2);
        if (node instanceof TemplateNode) {
            System.out.println(pad + "Template");
            for (var e : ((TemplateNode) node).elements) print(e, indent+1);
        } else if (node instanceof TextNode) {
            System.out.println(pad + node);
        } else if (node instanceof TemplateElementNode) {
            System.out.println(pad + node.getClass().getSimpleName() + ": " + node);
            // print inner expression/body when helpful
            if (node instanceof EchoNode) print(((EchoNode) node).expr, indent+1);
            if (node instanceof RawEchoNode) print(((RawEchoNode) node).expr, indent+1);
            if (node instanceof IfNode) {
                IfNode i = (IfNode) node;
                System.out.println(pad + "  Cond:");
                print(i.condition, indent+2);
                System.out.println(pad + "  Then:");
                for (var e : i.thenBranch) print(e, indent+2);
                for (var ei : i.elseIfs) {
                    System.out.println(pad + "  ElseIf cond:");
                    print(ei.condition, indent+2);
                    System.out.println(pad + "  ElseIf body:");
                    for (var e : ei.body) print(e, indent+2);
                }
                if (!i.elseBranch.isEmpty()) {
                    System.out.println(pad + "  Else:");
                    for (var e : i.elseBranch) print(e, indent+2);
                }
            }
            if (node instanceof ForeachNode) {
                ForeachNode f = (ForeachNode) node;
                System.out.println(pad + "  Iterable:");
                print(f.iterable, indent+2);
                System.out.println(pad + "  Body:");
                for (var e : f.body) print(e, indent+2);
            }
            if (node instanceof ForNode) {
                ForNode f = (ForNode) node;
                System.out.println(pad + "  Init: " + f.initIdentifier + " = ");
                print(f.initExpr, indent+2);
                System.out.println(pad + "  Cond:");
                print(f.condition, indent+2);
                System.out.println(pad + "  Body:");
                for (var e : f.body) print(e, indent+2);
            }
        } else if (node instanceof ExpressionNode) {
            System.out.println(pad + node.getClass().getSimpleName() + ": " + node);
            if (node instanceof BinaryOpNode) {
                print(((BinaryOpNode) node).left, indent+1);
                print(((BinaryOpNode) node).right, indent+1);
            } else if (node instanceof UnaryOpNode) {
                print(((UnaryOpNode) node).expr, indent+1);
            } else if (node instanceof MemberAccessNode) {
                print(((MemberAccessNode) node).left, indent+1);
            } else if (node instanceof FunctionCallNode) {
                FunctionCallNode f = (FunctionCallNode) node;
                System.out.println(pad + "  Callee:");
                print(f.callee, indent+2);
                System.out.println(pad + "  Args:");
                for (var a : f.args) print(a, indent+2);
            }
        } else {
            System.out.println(pad + node);
        }
    }
}
