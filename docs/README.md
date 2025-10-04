# Java Hypertext Preprocessor (JHP)

## Introduction

Java Hypertext Preprocessor (JHP) is a powerful, flexible template engine for Java applications. Inspired by PHP and modern template engines like Jinja2 and Blade, JHP provides an elegant syntax for embedding dynamic content in your HTML templates.

## Features

- **Intuitive Syntax** - Clean, readable template syntax with `{{ }}` for output and `{% %}` for logic
- **HTML Escaping** - Automatic HTML escaping by default to prevent XSS attacks
- **Control Structures** - Full support for if/else, for, foreach, and while loops
- **Template Includes** - Modular templates with include directives
- **Built-in Functions** - Common string and utility functions out of the box
- **Extensible** - Easy to add custom functions
- **Thread-Safe** - Built for concurrent environments
- **Template Caching** - Automatic AST caching for improved performance
- **Flexible Error Handling** - Multiple modes for handling runtime issues

## Documentation

- [Installation](installation.md)
- [Getting Started](getting-started.md)
- [Template Syntax](template-syntax.md)
- [Control Structures](control-structures.md)
- [Functions](functions.md)
- [Context & Variables](context-variables.md)
- [Configuration](configuration.md)
- [Advanced Usage](advanced-usage.md)
- [API Reference](api-reference.md)
- [Examples](examples.md)

## Quick Example

```java
// Create engine
Settings settings = Settings.builder()
    .base("/path/to/templates")
    .build();
FunctionLibrary lib = new FunctionLibrary();
JhpEngine engine = new JhpEngine(settings, lib);

// Prepare context
Context ctx = new Context();
ctx.add("name", "Alice");
ctx.add("age", 25);

// Render template
String output = engine.render("welcome.jhp", ctx);
```

**Template (welcome.jhp):**
```html
<h1>Hello {{ name }}!</h1>
{% if (age >= 18) %}
    <p>You are an adult.</p>
{% else %}
    <p>You are a minor.</p>
{% endif %}
```

## License

This project is open source. Check the repository for license details.

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues.
