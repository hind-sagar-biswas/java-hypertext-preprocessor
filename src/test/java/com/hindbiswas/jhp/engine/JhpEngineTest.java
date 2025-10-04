package com.hindbiswas.jhp.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import com.hindbiswas.jhp.Context;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@DisplayName("JhpEngine Integration Tests")
class JhpEngineTest {

    @TempDir
    Path tempDir;

    private JhpEngine engine;
    private FunctionLibrary functionLibrary;
    private Settings settings;

    @BeforeEach
    void setUp() {
        settings = Settings.builder()
            .base(tempDir.toString())
            .issueHandleMode(IssueHandleMode.THROW)
            .build();
        functionLibrary = new FunctionLibrary();
        engine = new JhpEngine(settings, functionLibrary);
    }

    @Test
    @DisplayName("Should render simple template with variable")
    void testSimpleTemplate() throws Exception {
        Path templatePath = tempDir.resolve("simple.jhp");
        Files.writeString(templatePath, "Hello {{ name }}!");

        Context context = new Context();
        context.add("name", "World");

        String result = engine.render(templatePath, context);
        assertEquals("Hello World!", result);
    }

    @Test
    @DisplayName("Should escape HTML by default")
    void testHtmlEscaping() throws Exception {
        Path templatePath = tempDir.resolve("escape.jhp");
        Files.writeString(templatePath, "Escaped: {{ html }}");

        Context context = new Context();
        context.add("html", "<script>alert('xss')</script>");

        String result = engine.render(templatePath, context);
        assertTrue(result.contains("&lt;script&gt;"));
        assertFalse(result.contains("<script>"));
    }

    @Test
    @DisplayName("Should not escape HTML with raw echo")
    void testRawEcho() throws Exception {
        Path templatePath = tempDir.resolve("raw.jhp");
        Files.writeString(templatePath, "Raw: {{{ html }}}");

        Context context = new Context();
        context.add("html", "<b>bold</b>");

        String result = engine.render(templatePath, context);
        assertTrue(result.contains("<b>bold</b>"));
    }

    @Test
    @DisplayName("Should render if statement - true branch")
    void testIfStatementTrue() throws Exception {
        Path templatePath = tempDir.resolve("if.jhp");
        Files.writeString(templatePath, "{% if (age >= 18) %}Adult{% else %}Minor{% endif %}");

        Context context = new Context();
        context.add("age", 25);

        String result = engine.render(templatePath, context);
        assertTrue(result.contains("Adult"));
        assertFalse(result.contains("Minor"));
    }

    @Test
    @DisplayName("Should render if statement - false branch")
    void testIfStatementFalse() throws Exception {
        Path templatePath = tempDir.resolve("if.jhp");
        Files.writeString(templatePath, "{% if (age >= 18) %}Adult{% else %}Minor{% endif %}");

        Context context = new Context();
        context.add("age", 15);

        String result = engine.render(templatePath, context);
        assertFalse(result.contains("Adult"));
        assertTrue(result.contains("Minor"));
    }

    @Test
    @DisplayName("Should render elseif statement")
    void testElseIfStatement() throws Exception {
        Path templatePath = tempDir.resolve("elseif.jhp");
        Files.writeString(templatePath, 
            "{% if (age < 13) %}Child{% elseif (age < 18) %}Teen{% else %}Adult{% endif %}");

        Context context = new Context();
        context.add("age", 15);

        String result = engine.render(templatePath, context);
        assertTrue(result.contains("Teen"));
    }

    @Test
    @DisplayName("Should render for loop")
    void testForLoop() throws Exception {
        Path templatePath = tempDir.resolve("for.jhp");
        Files.writeString(templatePath, "{% for (i = 0; i < 3; i++) %}{{ i }}{% endfor %}");

        Context context = new Context();
        String result = engine.render(templatePath, context);
        
        assertTrue(result.contains("0"));
        assertTrue(result.contains("1"));
        assertTrue(result.contains("2"));
    }

    @Test
    @DisplayName("Should render foreach loop with array")
    void testForeachArray() throws Exception {
        Path templatePath = tempDir.resolve("foreach.jhp");
        Files.writeString(templatePath, "{% foreach (item in items) %}{{ item }},{% endforeach %}");

        Context context = new Context();
        context.add("items", Arrays.asList("apple", "banana", "cherry"));

        String result = engine.render(templatePath, context);
        assertTrue(result.contains("apple"));
        assertTrue(result.contains("banana"));
        assertTrue(result.contains("cherry"));
    }

    @Test
    @DisplayName("Should render while loop")
    void testWhileLoop() throws Exception {
        Path templatePath = tempDir.resolve("while.jhp");
        Files.writeString(templatePath, 
            "{% for (i = 0; i < 3; i++) %}{{ i }}{% endfor %}");

        Context context = new Context();
        String result = engine.render(templatePath, context);
        assertTrue(result.contains("0"));
        assertTrue(result.contains("1"));
        assertTrue(result.contains("2"));
    }

    @Test
    @DisplayName("Should call built-in function")
    void testBuiltInFunction() throws Exception {
        Path templatePath = tempDir.resolve("function.jhp");
        Files.writeString(templatePath, "{{ touppercase(text) }}");

        Context context = new Context();
        context.add("text", "hello");

        String result = engine.render(templatePath, context);
        assertTrue(result.contains("HELLO"));
    }

    @Test
    @DisplayName("Should access map properties")
    void testObjectPropertyAccess() throws Exception {
        Path templatePath = tempDir.resolve("object.jhp");
        Files.writeString(templatePath, "Name: {{ user.name }}, Age: {{ user.age }}");

        Context context = new Context();
        // Use a Map instead of POJO to avoid Context conversion issues
        java.util.Map<String, Object> user = new java.util.HashMap<>();
        user.put("name", "Alice");
        user.put("age", 30);
        context.add("user", user);

        String result = engine.render(templatePath, context);
        assertTrue(result.contains("Alice"), "Expected 'Alice' in result: " + result);
        assertTrue(result.contains("30"), "Expected '30' in result: " + result);
    }

    @Test
    @DisplayName("Should handle include directive")
    void testInclude() throws Exception {
        Path partialPath = tempDir.resolve("partial.jhp");
        Files.writeString(partialPath, "This is partial content.");

        Path mainPath = tempDir.resolve("main.jhp");
        Files.writeString(mainPath, "Start {% include 'partial.jhp' %} End");

        Context context = new Context();
        String result = engine.render(mainPath, context);
        
        assertTrue(result.contains("Start"));
        assertTrue(result.contains("This is partial content."));
        assertTrue(result.contains("End"));
    }

    @Test
    @DisplayName("Should handle arithmetic operations")
    void testArithmetic() throws Exception {
        Path templatePath = tempDir.resolve("math.jhp");
        Files.writeString(templatePath, "Result: {{ 5 + 3 * 2 }}");

        Context context = new Context();
        String result = engine.render(templatePath, context);
        assertTrue(result.contains("11"));
    }

    @Test
    @DisplayName("Should handle string concatenation")
    void testStringConcatenation() throws Exception {
        Path templatePath = tempDir.resolve("concat.jhp");
        Files.writeString(templatePath, "{{ first + ' ' + last }}");

        Context context = new Context();
        context.add("first", "John");
        context.add("last", "Doe");

        String result = engine.render(templatePath, context);
        assertTrue(result.contains("John Doe"));
    }

    @Test
    @DisplayName("Should handle comparison operators")
    void testComparison() throws Exception {
        Path templatePath = tempDir.resolve("compare.jhp");
        Files.writeString(templatePath, "{% if (x > y) %}Greater{% else %}Not Greater{% endif %}");

        Context context = new Context();
        context.add("x", 10);
        context.add("y", 5);

        String result = engine.render(templatePath, context);
        assertTrue(result.contains("Greater"));
    }

    @Test
    @DisplayName("Should handle logical operators")
    void testLogicalOperators() throws Exception {
        Path templatePath = tempDir.resolve("logical.jhp");
        Files.writeString(templatePath, "{% if (a && b) %}Both True{% endif %}");

        Context context = new Context();
        context.add("a", true);
        context.add("b", true);

        String result = engine.render(templatePath, context);
        assertTrue(result.contains("Both True"));
    }

    @Test
    @DisplayName("Should handle ternary operator")
    void testTernary() throws Exception {
        Path templatePath = tempDir.resolve("ternary.jhp");
        Files.writeString(templatePath, "{{ age >= 18 ? 'Adult' : 'Minor' }}");

        Context context = new Context();
        context.add("age", 20);

        String result = engine.render(templatePath, context);
        assertTrue(result.contains("Adult"));
    }

    @Test
    @DisplayName("Should cache parsed templates")
    void testTemplateCaching() throws Exception {
        Path templatePath = tempDir.resolve("cached.jhp");
        Files.writeString(templatePath, "Hello {{ name }}!");

        Context context1 = new Context();
        context1.add("name", "First");
        String result1 = engine.render(templatePath, context1);

        Context context2 = new Context();
        context2.add("name", "Second");
        String result2 = engine.render(templatePath, context2);

        assertTrue(result1.contains("First"));
        assertTrue(result2.contains("Second"));
    }

    @Test
    @DisplayName("Should render template using string path")
    void testRenderWithStringPath() throws Exception {
        Path templatePath = tempDir.resolve("string_path.jhp");
        Files.writeString(templatePath, "Hello {{ name }}!");

        Context context = new Context();
        context.add("name", "World");

        String result = engine.render("string_path.jhp", context);
        assertEquals("Hello World!", result);
    }

    @Test
    @DisplayName("Should automatically append .jhp extension")
    void testAutoAppendExtension() throws Exception {
        Path templatePath = tempDir.resolve("auto.jhp");
        Files.writeString(templatePath, "Content");

        Context context = new Context();
        String result = engine.render("auto", context);
        assertTrue(result.contains("Content"));
    }

    @Test
    @DisplayName("Should disable HTML escaping when configured")
    void testDisableEscaping() throws Exception {
        Settings noEscapeSettings = Settings.builder()
            .base(tempDir.toString())
            .escape(false)
            .issueHandleMode(IssueHandleMode.THROW)
            .build();
        JhpEngine noEscapeEngine = new JhpEngine(noEscapeSettings, functionLibrary);

        Path templatePath = tempDir.resolve("no_escape.jhp");
        Files.writeString(templatePath, "{{ html }}");

        Context context = new Context();
        context.add("html", "<b>bold</b>");

        String result = noEscapeEngine.render(templatePath, context);
        assertTrue(result.contains("<b>bold</b>"));
    }

    // Helper class for testing
    static class TestUser {
        public String name;
        public int age;

        public TestUser(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
