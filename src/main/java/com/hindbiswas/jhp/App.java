package com.hindbiswas.jhp;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import com.hindbiswas.jhp.ast.AstBuilder;
import com.hindbiswas.jhp.ast.AstPrettyPrinter;
import com.hindbiswas.jhp.ast.AstRenderer;
import com.hindbiswas.jhp.ast.TemplateNode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {
        ParseTree tree = generateTree("/home/shinigami/Java/java-hypertext-preprocessor/examples/initial.jhp");
        AstBuilder builder = new AstBuilder();
        TemplateNode ast = (TemplateNode) builder.visit(tree);
        AstPrettyPrinter.print(ast);

        // Context
        Map<String, Object> ctx = new HashMap<>();
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Alice");
        user.put("age", 150);
        user.put("gender", "F");
        ctx.put("user", user);

        // render
        AstRenderer renderer = new AstRenderer(Path.of("examples")); // optional base dir
        String out = renderer.render(ast, ctx);
        System.out.println(out);

    }

    public static ParseTree generateTree(String file) throws Exception {
        // Read template file
        String text = Files.readString(Paths.get(file));

        // Create CharStream
        CharStream cs = CharStreams.fromString(text);

        // Lexer & token stream
        JhpTemplateLexer lexer = new JhpTemplateLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Parser
        JhpTemplateParser parser = new JhpTemplateParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new DiagnosticErrorListener());

        // Parse template
        ParseTree tree = parser.template();

        return tree;
    }
}
