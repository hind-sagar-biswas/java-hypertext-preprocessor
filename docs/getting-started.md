# Getting Started

- [Introduction](#introduction)
- [Basic Usage](#basic-usage)
- [Creating Your First Template](#creating-your-first-template)
- [Understanding Context](#understanding-context)
- [Rendering Templates](#rendering-templates)
- [Common Patterns](#common-patterns)

<a name="introduction"></a>
## Introduction

JHP makes it easy to create dynamic HTML content by separating your presentation logic from your business logic. Templates are written in `.jhp` files and can contain both static HTML and dynamic expressions.

<a name="basic-usage"></a>
## Basic Usage

The typical workflow for using JHP involves three steps:

1. **Configure the engine** with settings
2. **Prepare your context** with data
3. **Render the template** to produce output

### Minimal Example

```java
import com.hindbiswas.jhp.*;
import com.hindbiswas.jhp.engine.*;

public class Example {
    public static void main(String[] args) throws Exception {
        // 1. Configure engine
        Settings settings = Settings.builder()
            .base("/path/to/templates")
            .build();
        FunctionLibrary lib = new FunctionLibrary();
        JhpEngine engine = new JhpEngine(settings, lib);
        
        // 2. Prepare context
        Context ctx = new Context();
        ctx.add("name", "World");
        
        // 3. Render template
        String output = engine.render("hello.jhp", ctx);
        System.out.println(output);
    }
}
```

<a name="creating-your-first-template"></a>
## Creating Your First Template

Create a file named `hello.jhp` in your templates directory:

```html
<!DOCTYPE html>
<html>
<head>
    <title>Hello {{ name }}</title>
</head>
<body>
    <h1>Hello, {{ name }}!</h1>
    <p>Welcome to JHP.</p>
</body>
</html>
```

### Template Syntax Basics

JHP uses two types of delimiters:

- `{{ expression }}` - Outputs the result of an expression (HTML-escaped by default)
- `{{{ expression }}}` - Outputs raw HTML (not escaped)
- `{% statement %}` - Executes control flow statements

<a name="understanding-context"></a>
## Understanding Context

The `Context` object holds all variables that will be available in your template.

### Adding Simple Values

```java
Context ctx = new Context();
ctx.add("title", "My Page");
ctx.add("count", 42);
ctx.add("active", true);
```

### Adding Collections

```java
List<String> items = Arrays.asList("Apple", "Banana", "Cherry");
ctx.add("fruits", items);
```

### Adding Maps

```java
Map<String, Object> user = new HashMap<>();
user.put("name", "Alice");
user.put("email", "alice@example.com");
ctx.add("user", user);
```

### Adding POJOs

```java
class User {
    public String name;
    public int age;
    
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

User user = new User("Bob", 30);
ctx.add("user", user);
```

> **Note:** Context automatically converts POJOs to Maps using public fields.

<a name="rendering-templates"></a>
## Rendering Templates

### Rendering by Path

```java
// Using Path object
Path templatePath = Path.of("/templates/page.jhp");
String output = engine.render(templatePath, ctx);

// Using string path (relative to base directory)
String output = engine.render("page.jhp", ctx);
```

### Automatic .jhp Extension

JHP automatically appends `.jhp` extension if not provided:

```java
// These are equivalent:
engine.render("page", ctx);
engine.render("page.jhp", ctx);
```

### Rendering to Different Outputs

```java
// To string
String html = engine.render("template.jhp", ctx);

// Write to file
String html = engine.render("template.jhp", ctx);
Files.writeString(Path.of("output.html"), html);

// Write to response (in web framework)
response.getWriter().write(html);
```

<a name="common-patterns"></a>
## Common Patterns

### Reusable Engine Instance

Create a single engine instance and reuse it (thread-safe):

```java
public class TemplateEngine {
    private static final JhpEngine engine;
    
    static {
        Settings settings = Settings.builder()
            .base("src/main/resources/templates")
            .build();
        FunctionLibrary lib = new FunctionLibrary();
        engine = new JhpEngine(settings, lib);
    }
    
    public static String render(String template, Context ctx) throws Exception {
        return engine.render(template, ctx);
    }
}
```

### Template with Layout

**layout.jhp:**
```html
<!DOCTYPE html>
<html>
<head>
    <title>{{ title }}</title>
</head>
<body>
    {% include 'header.jhp' %}
    <main>
        <!-- Page content goes here -->
    </main>
    {% include 'footer.jhp' %}
</body>
</html>
```

### Error Handling

```java
try {
    String output = engine.render("template.jhp", ctx);
} catch (Exception e) {
    System.err.println("Template rendering failed: " + e.getMessage());
    e.printStackTrace();
}
```

## Next Steps

- Learn about [Template Syntax](template-syntax.md) in detail
- Explore [Control Structures](control-structures.md)
- Discover available [Functions](functions.md)
- Configure [Settings](configuration.md) for your needs
