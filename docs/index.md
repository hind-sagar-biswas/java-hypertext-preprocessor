# Java Hypertext Preprocessor Documentation

Welcome to the Java Hypertext Preprocessor (JHP) documentation!

## Table of Contents

### Getting Started
- [Introduction](README.md) - Overview and quick start
- [Installation](installation.md) - How to install and set up JHP
- [Getting Started](getting-started.md) - Your first JHP template

### Core Concepts
- [Template Syntax](template-syntax.md) - Learn the template language
- [Control Structures](control-structures.md) - If statements, loops, and includes
- [Functions](functions.md) - Built-in and custom functions
- [Context & Variables](context-variables.md) - Working with data

### Configuration & Advanced
- [Configuration](configuration.md) - Settings and options
- [Advanced Usage](advanced-usage.md) - Performance, threading, and patterns
- [API Reference](api-reference.md) - Complete API documentation
- [Examples](examples.md) - Real-world examples

## Quick Links

- **GitHub Repository**: [java-hypertext-preprocessor](https://github.com/hind-sagar-biswas/java-hypertext-preprocessor/)
- **Issue Tracker**: Report bugs and request features
- **Discussions**: Ask questions and share ideas

## Quick Example

```java
Settings settings = Settings.builder()
    .base("/path/to/templates")
    .build();
FunctionLibrary lib = new FunctionLibrary();
JhpEngine engine = new JhpEngine(settings, lib);

Context ctx = new Context();
ctx.add("name", "World");

String output = engine.render("hello.jhp", ctx);
```

## Need Help?

- Check the [API Reference](api-reference.md) for detailed class documentation
- Browse [Examples](examples.md) for common use cases
- Review [Advanced Usage](advanced-usage.md) for best practices
