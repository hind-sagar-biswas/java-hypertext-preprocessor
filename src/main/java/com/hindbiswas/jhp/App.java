package com.hindbiswas.jhp;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) throws Exception {
        ParseTree tree = generateTree("/home/shinigami/Java/java-hypertext-preprocessor/examples/initial.jhp");
        PrintableTree printableTree = new PrintableTree(tree);
        System.out.println(printableTree);
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
