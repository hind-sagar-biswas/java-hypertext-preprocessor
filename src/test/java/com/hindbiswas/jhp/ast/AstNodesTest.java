package com.hindbiswas.jhp.ast;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("AST Nodes Tests")
class AstNodesTest {

    @Test
    @DisplayName("Should create TextNode")
    void testTextNode() {
        TextNode node = new TextNode("Hello World");
        assertNotNull(node);
        assertEquals("Hello World", node.text);
        assertTrue(node.toString().contains("Text"));
    }

    @Test
    @DisplayName("Should create EchoNode")
    void testEchoNode() {
        LiteralNode literal = new LiteralNode("test");
        EchoNode node = new EchoNode(literal);
        assertNotNull(node);
        assertEquals(literal, node.expr);
        assertTrue(node.toString().contains("Echo"));
    }

    @Test
    @DisplayName("Should create RawEchoNode")
    void testRawEchoNode() {
        LiteralNode literal = new LiteralNode("test");
        RawEchoNode node = new RawEchoNode(literal);
        assertNotNull(node);
        assertEquals(literal, node.expr);
        assertTrue(node.toString().contains("RawEcho"));
    }

    @Test
    @DisplayName("Should create IfNode")
    void testIfNode() {
        LiteralNode condition = new LiteralNode(true);
        IfNode node = new IfNode(condition);
        assertNotNull(node);
        assertEquals(condition, node.condition);
        assertNotNull(node.thenBranch);
        assertNotNull(node.elseIfs);
        assertNotNull(node.elseBranch);
        assertTrue(node.toString().contains("If"));
    }

    @Test
    @DisplayName("Should create ElseIfPart")
    void testElseIfPart() {
        LiteralNode condition = new LiteralNode(true);
        ElseIfPart part = new ElseIfPart(condition);
        assertNotNull(part);
        assertEquals(condition, part.condition);
        assertNotNull(part.body);
        assertTrue(part.toString().contains("ElseIf"));
    }

    @Test
    @DisplayName("Should create ForNode")
    void testForNode() {
        LiteralNode initExpr = new LiteralNode(0);
        LiteralNode condition = new LiteralNode(true);
        ForNode node = new ForNode("i", initExpr, condition, "i", "++", null);
        assertNotNull(node);
        assertEquals("i", node.initIdentifier);
        assertEquals(initExpr, node.initExpr);
        assertEquals(condition, node.condition);
        assertEquals("++", node.updateOp);
        assertNotNull(node.body);
        assertTrue(node.toString().contains("For"));
    }

    @Test
    @DisplayName("Should create ForeachNode")
    void testForeachNode() {
        IdentifierNode iterable = new IdentifierNode("items");
        ForeachNode node = new ForeachNode("key", "value", iterable);
        assertNotNull(node);
        assertEquals("key", node.keyIdentifier);
        assertEquals("value", node.valueIdentifier);
        assertEquals(iterable, node.iterable);
        assertNotNull(node.body);
        assertTrue(node.toString().contains("Foreach"));
    }

    @Test
    @DisplayName("Should create WhileNode")
    void testWhileNode() {
        LiteralNode condition = new LiteralNode(true);
        WhileNode node = new WhileNode(condition);
        assertNotNull(node);
        assertEquals(condition, node.condition);
        assertNotNull(node.body);
        assertTrue(node.toString().contains("While"));
    }

    @Test
    @DisplayName("Should create BreakNode")
    void testBreakNode() {
        BreakNode node = new BreakNode();
        assertNotNull(node);
        assertTrue(node.toString().contains("Break"));
    }

    @Test
    @DisplayName("Should create ContinueNode")
    void testContinueNode() {
        ContinueNode node = new ContinueNode();
        assertNotNull(node);
        assertTrue(node.toString().contains("Continue"));
    }

    @Test
    @DisplayName("Should create IncludeNode")
    void testIncludeNode() {
        IncludeNode node = new IncludeNode("partial.jhp");
        assertNotNull(node);
        assertEquals("partial.jhp", node.path);
        assertTrue(node.toString().contains("Include"));
    }

    @Test
    @DisplayName("Should create LiteralNode with string")
    void testLiteralNodeString() {
        LiteralNode node = new LiteralNode("hello");
        assertNotNull(node);
        assertEquals("hello", node.value);
        assertTrue(node.toString().contains("Literal"));
    }

    @Test
    @DisplayName("Should create LiteralNode with number")
    void testLiteralNodeNumber() {
        LiteralNode node = new LiteralNode(42);
        assertNotNull(node);
        assertEquals(42, node.value);
    }

    @Test
    @DisplayName("Should create LiteralNode with boolean")
    void testLiteralNodeBoolean() {
        LiteralNode node = new LiteralNode(true);
        assertNotNull(node);
        assertEquals(true, node.value);
    }

    @Test
    @DisplayName("Should create IdentifierNode")
    void testIdentifierNode() {
        IdentifierNode node = new IdentifierNode("myVar");
        assertNotNull(node);
        assertEquals("myVar", node.name);
        assertTrue(node.toString().contains("Ident"));
    }

    @Test
    @DisplayName("Should create MemberAccessNode")
    void testMemberAccessNode() {
        IdentifierNode left = new IdentifierNode("user");
        MemberAccessNode node = new MemberAccessNode(left, "name");
        assertNotNull(node);
        assertEquals(left, node.left);
        assertEquals("name", node.member);
        assertTrue(node.toString().contains("Member"));
    }

    @Test
    @DisplayName("Should create ArrayAccessNode")
    void testArrayAccessNode() {
        IdentifierNode array = new IdentifierNode("items");
        LiteralNode index = new LiteralNode(0);
        ArrayAccessNode node = new ArrayAccessNode(array, index);
        assertNotNull(node);
        assertEquals(array, node.array);
        assertEquals(index, node.index);
        assertTrue(node.toString().contains("Index"));
    }

    @Test
    @DisplayName("Should create FunctionCallNode")
    void testFunctionCallNode() {
        IdentifierNode callee = new IdentifierNode("myFunc");
        FunctionCallNode node = new FunctionCallNode(callee);
        assertNotNull(node);
        assertEquals(callee, node.callee);
        assertNotNull(node.args);
        assertTrue(node.toString().contains("Call"));
    }

    @Test
    @DisplayName("Should create UnaryOpNode")
    void testUnaryOpNode() {
        LiteralNode expr = new LiteralNode(5);
        UnaryOpNode node = new UnaryOpNode("-", expr);
        assertNotNull(node);
        assertEquals("-", node.op);
        assertEquals(expr, node.expr);
        assertTrue(node.toString().contains("Unary"));
    }

    @Test
    @DisplayName("Should create BinaryOpNode")
    void testBinaryOpNode() {
        LiteralNode left = new LiteralNode(5);
        LiteralNode right = new LiteralNode(3);
        BinaryOpNode node = new BinaryOpNode(left, "+", right);
        assertNotNull(node);
        assertEquals(left, node.left);
        assertEquals("+", node.op);
        assertEquals(right, node.right);
        assertTrue(node.toString().contains("Binary"));
    }

    @Test
    @DisplayName("Should create TernaryNode")
    void testTernaryNode() {
        LiteralNode cond = new LiteralNode(true);
        LiteralNode thenExpr = new LiteralNode("yes");
        LiteralNode elseExpr = new LiteralNode("no");
        TernaryNode node = new TernaryNode(cond, thenExpr, elseExpr);
        assertNotNull(node);
        assertEquals(cond, node.cond);
        assertEquals(thenExpr, node.thenExpr);
        assertEquals(elseExpr, node.elseExpr);
        assertTrue(node.toString().contains("Ternary"));
    }

    @Test
    @DisplayName("Should create BlockNode")
    void testBlockNode() {
        BlockNode node = new BlockNode("myBlock");
        assertNotNull(node);
        assertEquals("myBlock", node.name);
        assertNotNull(node.body);
        assertTrue(node.toString().contains("Block"));
    }

    @Test
    @DisplayName("Should create NodeSpan")
    void testNodeSpan() {
        NodeSpan span = new NodeSpan(10, 20);
        assertNotNull(span);
        assertEquals(10, span.start);
        assertEquals(20, span.end);
        assertTrue(span.toString().contains("10"));
        assertTrue(span.toString().contains("20"));
    }

    @Test
    @DisplayName("Should create TemplateNode")
    void testTemplateNode() {
        TemplateNode node = new TemplateNode();
        assertNotNull(node);
        assertNotNull(node.elements);
        assertTrue(node.elements.isEmpty());
    }

    @Test
    @DisplayName("Should add elements to TemplateNode")
    void testTemplateNodeAddElements() {
        TemplateNode node = new TemplateNode();
        TextNode text = new TextNode("Hello");
        node.elements.add(text);
        assertEquals(1, node.elements.size());
        assertEquals(text, node.elements.get(0));
    }
}
