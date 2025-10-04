# Quick Reference

A quick reference guide for JHP template syntax and common patterns.

## Installation

```xml
<dependency>
    <groupId>com.hindbiswas.jhp</groupId>
    <artifactId>jhp</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Basic Setup

```java
Settings settings = Settings.builder()
    .base("/templates")
    .build();
FunctionLibrary lib = new FunctionLibrary();
JhpEngine engine = new JhpEngine(settings, lib);

Context ctx = new Context();
ctx.add("key", value);

String output = engine.render("template.jhp", ctx);
```

## Template Syntax

### Output

```html
{{ variable }}              <!-- Escaped output -->
{{{ rawHtml }}}            <!-- Raw output -->
{{ user.name }}            <!-- Member access -->
{{ items[0] }}             <!-- Array access -->
{{ 5 + 3 }}                <!-- Expression -->
```

### Control Structures

```html
<!-- If Statement -->
{% if (condition) %}
    ...
{% elseif (other) %}
    ...
{% else %}
    ...
{% endif %}

<!-- For Loop -->
{% for (i = 0; i < 10; i++) %}
    {{ i }}
{% endfor %}

<!-- Foreach Loop -->
{% foreach (item in items) %}
    {{ item }}
{% endforeach %}

<!-- Foreach with Key -->
{% foreach (key, value in map) %}
    {{ key }}: {{ value }}
{% endforeach %}

<!-- While Loop -->
{% while (condition) %}
    ...
{% endwhile %}

<!-- Break/Continue -->
{% break %}
{% continue %}

<!-- Include -->
{% include 'partial.jhp' %}
```

## Operators

### Arithmetic
```html
{{ a + b }}    <!-- Addition -->
{{ a - b }}    <!-- Subtraction -->
{{ a * b }}    <!-- Multiplication -->
{{ a / b }}    <!-- Division -->
{{ a % b }}    <!-- Modulo -->
```

### Comparison
```html
{{ a == b }}   <!-- Equal -->
{{ a != b }}   <!-- Not equal -->
{{ a > b }}    <!-- Greater than -->
{{ a < b }}    <!-- Less than -->
{{ a >= b }}   <!-- Greater or equal -->
{{ a <= b }}   <!-- Less or equal -->
```

### Logical
```html
{{ a && b }}   <!-- AND -->
{{ a || b }}   <!-- OR -->
{{ !a }}       <!-- NOT -->
```

### Ternary
```html
{{ condition ? "yes" : "no" }}
```

## Built-in Functions

```html
{{ touppercase(text) }}        <!-- HELLO -->
{{ tolowercase(text) }}        <!-- hello -->
{{ trim(text) }}               <!-- Remove whitespace -->
{{ len(items) }}               <!-- Length/size -->
{{ now() }}                    <!-- Current timestamp -->
```

## Configuration

```java
Settings settings = Settings.builder()
    .base("/templates")              // Base directory
    .escape(true)                    // HTML escaping (default: true)
    .maxIncludeDepth(15)            // Max include depth (default: 15)
    .issueHandleMode(IssueHandleMode.COMMENT)  // Error mode
    .build();
```

### Issue Handle Modes
- `COMMENT` - Render as HTML comments (default)
- `THROW` - Throw exceptions
- `DEBUG` - Show detailed debug info
- `IGNORE` - Silently ignore

## Custom Functions

```java
FunctionLibrary lib = new FunctionLibrary();

lib.register("double", (args, scopes) -> {
    Number n = (Number) args.get(0);
    return n.doubleValue() * 2;
});

// Use in template
{{ double(5) }}  <!-- 10.0 -->
```

## Context

```java
Context ctx = new Context();

// Simple values
ctx.add("name", "Alice");
ctx.add("age", 25);

// Collections
ctx.add("items", Arrays.asList("A", "B", "C"));

// Maps
Map<String, Object> user = new HashMap<>();
user.put("name", "Bob");
user.put("email", "bob@example.com");
ctx.add("user", user);
```

## Common Patterns

### Layout with Includes

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
        <!-- Content -->
    </main>
    {% include 'footer.jhp' %}
</body>
</html>
```

### Loop with Index

```html
<ul>
{% for (i = 0; i < len(items); i++) %}
    <li>{{ i + 1 }}. {{ items[i] }}</li>
{% endfor %}
</ul>
```

### Conditional Classes

```html
<div class="item {{ active ? 'active' : '' }}">
    {{ name }}
</div>
```

### Safe Navigation

```html
{% if (user) %}
    {{ user.name }}
{% else %}
    Guest
{% endif %}
```

## Error Handling

```java
try {
    String output = engine.render("template.jhp", ctx);
} catch (Exception e) {
    // Handle error
}
```

## Thread Safety

```java
// Single engine instance (thread-safe)
private static final JhpEngine engine = new JhpEngine(settings, lib);

// Create context per request
public String render(String template, Map<String, Object> data) {
    Context ctx = new Context();
    ctx.getContext().putAll(data);
    return engine.render(template, ctx);
}
```

## Performance Tips

1. **Reuse engine instance** - Create once, use many times
2. **Use template caching** - Automatic, no configuration needed
3. **Minimize include depth** - Keep templates flat when possible
4. **Prepare data in Java** - Don't do complex logic in templates
5. **Use Maps over POJOs** - For better performance with nested data

## See Also

- [Template Syntax](template-syntax.md) - Complete syntax guide
- [Control Structures](control-structures.md) - Detailed control flow
- [Functions](functions.md) - All built-in functions
- [Configuration](configuration.md) - All settings options
- [API Reference](api-reference.md) - Complete API docs
