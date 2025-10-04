# Template Syntax

- [Introduction](#introduction)
- [Displaying Data](#displaying-data)
- [HTML Escaping](#html-escaping)
- [Comments](#comments)
- [Variables](#variables)
- [Expressions](#expressions)
- [Operators](#operators)
- [Member Access](#member-access)
- [Array Access](#array-access)

<a name="introduction"></a>
## Introduction

JHP templates are plain text files with special syntax for dynamic content. They typically have a `.jhp` extension and can contain HTML, text, or any other format.

<a name="displaying-data"></a>
## Displaying Data

You can display data by wrapping variables in double curly braces:

```html
<h1>Hello, {{ name }}!</h1>
<p>You have {{ count }} messages.</p>
```

The `{{ }}` syntax automatically escapes HTML to prevent XSS attacks.

<a name="html-escaping"></a>
## HTML Escaping

### Escaped Output (Default)

By default, all output is HTML-escaped:

```html
{{ userInput }}
```

If `userInput` contains `<script>alert('xss')</script>`, it will be rendered as:
```html
&lt;script&gt;alert('xss')&lt;/script&gt;
```

### Raw Output

To output raw HTML without escaping, use triple curly braces:

```html
{{{ htmlContent }}}
```

**Example:**
```java
ctx.add("htmlContent", "<b>Bold Text</b>");
```

```html
Escaped: {{ htmlContent }}
<!-- Output: &lt;b&gt;Bold Text&lt;/b&gt; -->

Raw: {{{ htmlContent }}}
<!-- Output: <b>Bold Text</b> -->
```

> **Warning:** Only use raw output with trusted content to avoid XSS vulnerabilities.

### Disabling Escaping Globally

You can disable HTML escaping globally in settings:

```java
Settings settings = Settings.builder()
    .escape(false)
    .build();
```

<a name="comments"></a>
## Comments

JHP doesn't have built-in comment syntax, but you can use HTML comments:

```html
<!-- This is a comment -->
{{ name }}
```

<a name="variables"></a>
## Variables

Variables are accessed by name:

```html
{{ username }}
{{ age }}
{{ isActive }}
```

Variables must be added to the Context before rendering:

```java
Context ctx = new Context();
ctx.add("username", "Alice");
ctx.add("age", 25);
ctx.add("isActive", true);
```

<a name="expressions"></a>
## Expressions

JHP supports various types of expressions:

### Literals

```html
{{ "Hello World" }}
{{ 42 }}
{{ 3.14 }}
{{ true }}
{{ false }}
{{ null }}
```

### String Concatenation

```html
{{ "Hello " + name }}
{{ firstName + " " + lastName }}
```

### Arithmetic

```html
{{ 5 + 3 }}          <!-- 8 -->
{{ 10 - 4 }}         <!-- 6 -->
{{ 6 * 7 }}          <!-- 42 -->
{{ 20 / 4 }}         <!-- 5.0 -->
{{ 17 % 5 }}         <!-- 2.0 -->
{{ 2 + 3 * 4 }}      <!-- 14 (respects precedence) -->
{{ (2 + 3) * 4 }}    <!-- 20 -->
```

### Ternary Operator

```html
{{ age >= 18 ? "Adult" : "Minor" }}
{{ count > 0 ? count + " items" : "No items" }}
```

<a name="operators"></a>
## Operators

### Comparison Operators

```html
{{ x > y }}          <!-- Greater than -->
{{ x < y }}          <!-- Less than -->
{{ x >= y }}         <!-- Greater than or equal -->
{{ x <= y }}         <!-- Less than or equal -->
{{ x == y }}         <!-- Equal -->
{{ x != y }}         <!-- Not equal -->
```

### Logical Operators

```html
{{ isActive && isVerified }}     <!-- AND -->
{{ isAdmin || isModerator }}     <!-- OR -->
{{ !isDeleted }}                 <!-- NOT -->
```

### Unary Operators

```html
{{ -value }}         <!-- Negation -->
{{ !condition }}     <!-- Logical NOT -->
```

<a name="member-access"></a>
## Member Access

Access object properties using dot notation:

```html
{{ user.name }}
{{ user.email }}
{{ product.price }}
```

**Java Context:**
```java
Map<String, Object> user = new HashMap<>();
user.put("name", "Alice");
user.put("email", "alice@example.com");
ctx.add("user", user);
```

### Nested Access

```html
{{ user.address.city }}
{{ order.customer.name }}
```

<a name="array-access"></a>
## Array Access

Access array or list elements using square brackets:

```html
{{ items[0] }}
{{ items[1] }}
{{ items[index] }}
```

**Java Context:**
```java
List<String> items = Arrays.asList("Apple", "Banana", "Cherry");
ctx.add("items", items);
ctx.add("index", 1);
```

### Map Access

```html
{{ config["database"] }}
{{ settings["theme"] }}
```

## Complex Expressions

You can combine multiple operators and access methods:

```html
{{ (user.age >= 18 && user.verified) ? "Access Granted" : "Access Denied" }}
{{ items[0].name + " - $" + items[0].price }}
{{ (total * 0.9).toFixed(2) }}
```

## Function Calls

Call functions within expressions:

```html
{{ touppercase(name) }}
{{ len(items) }}
{{ trim(userInput) }}
{{ touppercase(user.name) + " (" + len(user.email) + ")" }}
```

See [Functions](functions.md) for a complete list of available functions.

## Whitespace Handling

JHP preserves whitespace in templates:

```html
<p>
    {{ name }}
</p>
```

This will include the newlines and indentation in the output.

## Next Steps

- Learn about [Control Structures](control-structures.md)
- Explore [Functions](functions.md)
- Understand [Context & Variables](context-variables.md)
