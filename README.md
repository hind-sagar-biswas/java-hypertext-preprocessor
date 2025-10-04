# Java Hypertext Preprocessor (JHP)

[![Maven Central](https://img.shields.io/maven-central/v/com.hindbiswas.jhp/jhp.svg?label=Maven%20Central)](https://search.maven.org/artifact/com.hindbiswas.jhp/jhp)
[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://www.oracle.com/java/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![GitHub release](https://img.shields.io/github/v/release/hind-sagar-biswas/java-hypertext-preprocessor.svg)](https://github.com/hind-sagar-biswas/java-hypertext-preprocessor/releases)

A powerful, flexible template engine for Java applications. Inspired by PHP and modern template engines like Jinja2 and Blade, JHP provides an elegant syntax for embedding dynamic content in your HTML templates.

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

## Quick Start

### Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.hindbiswas.jhp</groupId>
    <artifactId>jhp</artifactId>
    <version>1.0.0</version>
</dependency>
```

The artifact is available on [Maven Central](https://search.maven.org/artifact/com.hindbiswas.jhp/jhp).

### Basic Usage

```java
import com.hindbiswas.jhp.*;
import com.hindbiswas.jhp.engine.*;

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

## Template Syntax

### Output Variables

```html
{{ variable }}           <!-- HTML-escaped output -->
{{{ rawHtml }}}         <!-- Raw output (not escaped) -->
```

### Control Structures

```html
<!-- If Statement -->
{% if (condition) %}
    Content
{% elseif (otherCondition) %}
    Other content
{% else %}
    Default content
{% endif %}

<!-- For Loop -->
{% for (i = 0; i < 10; i++) %}
    <p>Item {{ i }}</p>
{% endfor %}

<!-- Foreach Loop -->
{% foreach (item in items) %}
    <li>{{ item }}</li>
{% endforeach %}

<!-- Include Template -->
{% include 'header.jhp' %}
```

### Functions

```html
{{ touppercase(name) }}
{{ tolowercase(email) }}
{{ trim(input) }}
{{ len(items) }}
{{ now() }}
```

## Documentation

Comprehensive documentation is available in the [`docs/`](docs/) directory:

- [Installation Guide](docs/installation.md)
- [Getting Started](docs/getting-started.md)
- [Template Syntax](docs/template-syntax.md)
- [Control Structures](docs/control-structures.md)
- [Functions](docs/functions.md)
- [Context & Variables](docs/context-variables.md)
- [Configuration](docs/configuration.md)
- [Advanced Usage](docs/advanced-usage.md)
- [API Reference](docs/api-reference.md)
- [Examples](docs/examples.md)

## Building from Source

```bash
# Clone the repository
git clone https://github.com/hind-sagar-biswas/java-hypertext-preprocessor.git
cd java-hypertext-preprocessor

# Build with Maven
mvn clean install

# Run tests
mvn test
```

## Requirements

- Java 25 or higher (Java 24 supported)
- Maven 3.6+
- ANTLR 4.13.2 (automatically managed)

## Testing

The project includes comprehensive unit tests using JUnit Jupiter:

```bash
mvn test
```

**Test Coverage:**
- 88 unit tests
- Context and variable management
- Settings and configuration
- Function library (built-in and custom)
- Template rendering and integration
- AST node structures

## Examples

### Web Application

```java
@GetMapping("/profile/{username}")
public ResponseEntity<String> profile(@PathVariable String username) {
    User user = userService.findByUsername(username);
    
    Context ctx = new Context();
    ctx.add("user", user.toMap());
    
    String html = engine.render("profile.jhp", ctx);
    return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
}
```

### Email Templates

```java
public void sendWelcomeEmail(User user) throws Exception {
    Context ctx = new Context();
    ctx.add("userName", user.getName());
    ctx.add("activationLink", generateActivationLink(user));
    
    String html = engine.render("welcome-email.jhp", ctx);
    emailClient.send(user.getEmail(), "Welcome!", html);
}
```

### Custom Functions

```java
FunctionLibrary lib = new FunctionLibrary();

lib.register("formatprice", (args, scopes) -> {
    Number price = (Number) args.get(0);
    return String.format("$%.2f", price.doubleValue());
});

JhpEngine engine = new JhpEngine(settings, lib);
```

**Template:**
```html
<span class="price">{{ formatprice(product.price) }}</span>
```

## Configuration

```java
Settings settings = Settings.builder()
    .base("/var/www/templates")           // Base directory
    .escape(true)                          // HTML escaping (default: true)
    .maxIncludeDepth(15)                   // Max include depth (default: 15)
    .issueHandleMode(IssueHandleMode.COMMENT)  // Error handling mode
    .build();
```

### Issue Handling Modes

- **COMMENT** - Render issues as HTML comments (default)
- **THROW** - Throw exceptions for issues
- **DEBUG** - Render detailed debug information
- **IGNORE** - Silently ignore issues

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Links

- **Documentation**: [docs/](docs/)
- **GitHub**: [java-hypertext-preprocessor](https://github.com/hind-sagar-biswas/java-hypertext-preprocessor/)
- **Maven Central**: [com.hindbiswas.jhp:jhp](https://search.maven.org/artifact/com.hindbiswas.jhp/jhp)
- **Issues**: [Issue Tracker](https://github.com/hind-sagar-biswas/java-hypertext-preprocessor/issues)
- **Publishing Guide**: [PUBLISHING.md](PUBLISHING.md)
- **Changelog**: [CHANGELOG.md](CHANGELOG.md)

## Acknowledgments

Inspired by:
- PHP's template syntax
- Jinja2 (Python)
- Blade (Laravel)
- Twig (Symfony)

---

Made with ❤️ by [Hind Sagar Biswas](https://github.com/hind-sagar-biswas)
