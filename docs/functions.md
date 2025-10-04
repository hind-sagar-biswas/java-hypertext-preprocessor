# Functions

- [Introduction](#introduction)
- [Built-in Functions](#built-in-functions)
- [String Functions](#string-functions)
- [Utility Functions](#utility-functions)
- [Custom Functions](#custom-functions)
- [Function Composition](#function-composition)

<a name="introduction"></a>
## Introduction

JHP provides a set of built-in functions and allows you to register custom functions. Functions are called within expressions using standard function call syntax.

<a name="built-in-functions"></a>
## Built-in Functions

JHP includes several built-in functions that are always available.

<a name="string-functions"></a>
## String Functions

### touppercase

Convert a string to uppercase.

**Syntax:**
```html
{{ touppercase(string) }}
```

**Example:**
```html
{{ touppercase("hello world") }}
<!-- Output: HELLO WORLD -->

{{ touppercase(username) }}
```

**Java Context:**
```java
ctx.add("username", "alice");
```

**Output:**
```
ALICE
```

### tolowercase

Convert a string to lowercase.

**Syntax:**
```html
{{ tolowercase(string) }}
```

**Example:**
```html
{{ tolowercase("HELLO WORLD") }}
<!-- Output: hello world -->

{{ tolowercase(email) }}
```

### trim

Remove leading and trailing whitespace from a string.

**Syntax:**
```html
{{ trim(string) }}
```

**Example:**
```html
{{ trim("  hello world  ") }}
<!-- Output: hello world -->

{{ trim(userInput) }}
```

**Java Context:**
```java
ctx.add("userInput", "  Alice  ");
```

**Output:**
```
Alice
```

<a name="utility-functions"></a>
## Utility Functions

### len

Get the length of a string, collection, map, or array.

**Syntax:**
```html
{{ len(value) }}
```

**Examples:**

**String Length:**
```html
{{ len("hello") }}
<!-- Output: 5 -->

{{ len(username) }}
```

**Collection Size:**
```html
{{ len(items) }}
```

**Java Context:**
```java
List<String> items = Arrays.asList("A", "B", "C");
ctx.add("items", items);
```

**Output:**
```
3
```

**Map Size:**
```html
{{ len(config) }}
```

**Array Length:**
```html
{{ len(numbers) }}
```

**Java Context:**
```java
int[] numbers = {1, 2, 3, 4, 5};
ctx.add("numbers", numbers);
```

**Output:**
```
5
```

### now

Get the current timestamp as an Instant object.

**Syntax:**
```html
{{ now() }}
```

**Example:**
```html
<p>Generated at: {{ now() }}</p>
<!-- Output: Generated at: 2025-10-05T00:18:03.123456Z -->
```

> **Note:** The output format depends on the Instant.toString() method.

<a name="custom-functions"></a>
## Custom Functions

You can register custom functions to extend JHP's functionality.

### Registering a Function

```java
FunctionLibrary lib = new FunctionLibrary();

// Register a simple function
lib.register("double", (args, scopes) -> {
    if (args.isEmpty()) return 0;
    Number n = (Number) args.get(0);
    return n.doubleValue() * 2;
});

JhpEngine engine = new JhpEngine(settings, lib);
```

**Template:**
```html
{{ double(5) }}
<!-- Output: 10.0 -->
```

### Function with Multiple Arguments

```java
lib.register("add", (args, scopes) -> {
    if (args.size() < 2) return 0;
    Number a = (Number) args.get(0);
    Number b = (Number) args.get(1);
    return a.doubleValue() + b.doubleValue();
});
```

**Template:**
```html
{{ add(10, 20) }}
<!-- Output: 30.0 -->
```

### String Manipulation Function

```java
lib.register("reverse", (args, scopes) -> {
    if (args.isEmpty() || args.get(0) == null) return "";
    String str = args.get(0).toString();
    return new StringBuilder(str).reverse().toString();
});
```

**Template:**
```html
{{ reverse("hello") }}
<!-- Output: olleh -->
```

### Function with Context Access

Functions can access the scope stack to read variables:

```java
lib.register("greet", (args, scopes) -> {
    String name = "Guest";
    
    // Search through scopes for 'username' variable
    for (Map<String, Object> scope : scopes) {
        if (scope.containsKey("username")) {
            name = scope.get("username").toString();
            break;
        }
    }
    
    return "Hello, " + name + "!";
});
```

**Template:**
```html
{{ greet() }}
<!-- Output: Hello, Alice! (if username is in context) -->
```

### Conditional Function

```java
lib.register("pluralize", (args, scopes) -> {
    if (args.size() < 2) return "";
    
    Number count = (Number) args.get(0);
    String singular = args.get(1).toString();
    String plural = args.size() > 2 ? args.get(2).toString() : singular + "s";
    
    return count.intValue() == 1 ? singular : plural;
});
```

**Template:**
```html
{{ count }} {{ pluralize(count, "item") }}
<!-- 1 item, 5 items -->

{{ count }} {{ pluralize(count, "person", "people") }}
<!-- 1 person, 5 people -->
```

### Unregistering Functions

```java
lib.unregister("myFunction");
```

### Overriding Built-in Functions

Custom functions take precedence over built-in functions:

```java
// Override the built-in trim function
lib.register("trim", (args, scopes) -> {
    if (args.isEmpty() || args.get(0) == null) return "";
    return args.get(0).toString().trim().toUpperCase();
});
```

<a name="function-composition"></a>
## Function Composition

You can nest function calls to create complex expressions:

```html
{{ touppercase(trim(username)) }}
<!-- Trim then uppercase -->

{{ len(tolowercase(text)) }}
<!-- Convert to lowercase then get length -->

{{ touppercase(items[0]) + " - " + len(items) + " items" }}
<!-- Combine function calls with operators -->
```

### Complex Example

```html
{% if (len(trim(comment)) > 0) %}
    <p>{{ touppercase(trim(comment)) }}</p>
{% endif %}
```

## Function Best Practices

1. **Keep Functions Pure** - Functions should not have side effects
2. **Handle Null Values** - Always check for null arguments
3. **Return Appropriate Types** - Return types that make sense in templates
4. **Use Descriptive Names** - Function names should be clear and lowercase
5. **Document Custom Functions** - Maintain documentation for your custom functions
6. **Error Handling** - Handle errors gracefully within functions

### Example: Well-Designed Function

```java
lib.register("formatprice", (args, scopes) -> {
    // Validate arguments
    if (args.isEmpty() || args.get(0) == null) {
        return "$0.00";
    }
    
    try {
        // Parse number
        Number price = (Number) args.get(0);
        
        // Format with 2 decimal places
        return String.format("$%.2f", price.doubleValue());
    } catch (Exception e) {
        // Return safe default on error
        return "$0.00";
    }
});
```

**Template:**
```html
<span class="price">{{ formatprice(product.price) }}</span>
```

## Case Insensitivity

Function names are case-insensitive:

```html
{{ TOUPPERCASE(text) }}
{{ ToUpperCase(text) }}
{{ touppercase(text) }}
<!-- All work the same -->
```

## Error Handling

If a function throws an exception, the behavior depends on the configured `IssueHandleMode`:

- **THROW** - Exception is propagated
- **COMMENT** - Error is rendered as HTML comment
- **DEBUG** - Detailed error information is displayed
- **IGNORE** - Error is silently ignored

See [Configuration](configuration.md) for more details.

## Next Steps

- Learn about [Context & Variables](context-variables.md)
- Explore [Configuration](configuration.md) options
- Review [Advanced Usage](advanced-usage.md)
