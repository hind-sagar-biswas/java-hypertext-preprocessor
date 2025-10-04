# Context & Variables

- [Introduction](#introduction)
- [Creating Context](#creating-context)
- [Adding Variables](#adding-variables)
- [Data Types](#data-types)
- [POJO Conversion](#pojo-conversion)
- [Variable Scope](#variable-scope)
- [Best Practices](#best-practices)

<a name="introduction"></a>
## Introduction

The `Context` object is the bridge between your Java code and JHP templates. It holds all the data that will be available to your templates during rendering.

<a name="creating-context"></a>
## Creating Context

Create a new context instance:

```java
Context ctx = new Context();
```

<a name="adding-variables"></a>
## Adding Variables

Add variables to the context using the `add()` method:

```java
ctx.add("variableName", value);
```

### Simple Example

```java
Context ctx = new Context();
ctx.add("title", "My Page");
ctx.add("count", 42);
ctx.add("active", true);
```

**Template:**
```html
<h1>{{ title }}</h1>
<p>Count: {{ count }}</p>
<p>Active: {{ active }}</p>
```

<a name="data-types"></a>
## Data Types

Context supports all common Java data types.

### Strings

```java
ctx.add("name", "Alice");
ctx.add("email", "alice@example.com");
```

### Numbers

```java
ctx.add("age", 25);              // Integer
ctx.add("price", 19.99);         // Double
ctx.add("count", 100L);          // Long
ctx.add("rating", 4.5f);         // Float
```

### Booleans

```java
ctx.add("isActive", true);
ctx.add("verified", false);
```

### Null Values

```java
ctx.add("optional", null);
```

**Template:**
```html
{{ optional }}
<!-- Renders as empty string -->
```

### Lists and Collections

```java
List<String> fruits = Arrays.asList("Apple", "Banana", "Cherry");
ctx.add("fruits", fruits);

Set<Integer> numbers = new HashSet<>(Arrays.asList(1, 2, 3));
ctx.add("numbers", numbers);
```

**Template:**
```html
{% foreach (fruit in fruits) %}
    <li>{{ fruit }}</li>
{% endforeach %}
```

### Arrays

```java
String[] colors = {"Red", "Green", "Blue"};
ctx.add("colors", colors);

int[] scores = {85, 90, 78, 92};
ctx.add("scores", scores);
```

**Template:**
```html
{{ colors[0] }}
{{ scores[1] }}
```

### Maps

```java
Map<String, Object> user = new HashMap<>();
user.put("name", "Bob");
user.put("age", 30);
user.put("email", "bob@example.com");
ctx.add("user", user);
```

**Template:**
```html
{{ user.name }}
{{ user.age }}
{{ user["email"] }}
```

<a name="pojo-conversion"></a>
## POJO Conversion

Context automatically converts Plain Old Java Objects (POJOs) to Maps.

### Basic POJO

```java
class User {
    public String name;
    public int age;
    public String email;
    
    public User(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
}

User user = new User("Alice", 25, "alice@example.com");
ctx.add("user", user);
```

**Template:**
```html
<h2>{{ user.name }}</h2>
<p>Age: {{ user.age }}</p>
<p>Email: {{ user.email }}</p>
```

### POJO Conversion Rules

1. **Only public fields** are converted
2. **Nested POJOs** are skipped (only primitives, strings, collections, and arrays)
3. **Collections and arrays** within POJOs are converted recursively

### Example with Collections

```java
class Product {
    public String name;
    public double price;
    public List<String> tags;
    
    public Product(String name, double price, List<String> tags) {
        this.name = name;
        this.price = price;
        this.tags = tags;
    }
}

Product product = new Product("Laptop", 999.99, Arrays.asList("electronics", "computers"));
ctx.add("product", product);
```

**Template:**
```html
<h3>{{ product.name }}</h3>
<p>${{ product.price }}</p>
<ul>
{% foreach (tag in product.tags) %}
    <li>{{ tag }}</li>
{% endforeach %}
</ul>
```

### Nested Objects Limitation

```java
class Address {
    public String city;
    public String country;
}

class User {
    public String name;
    public Address address;  // This will be skipped
}
```

**Workaround:** Convert nested objects to Maps manually:

```java
User user = new User();
user.name = "Alice";

Map<String, Object> userMap = new HashMap<>();
userMap.put("name", user.name);

Map<String, String> addressMap = new HashMap<>();
addressMap.put("city", user.address.city);
addressMap.put("country", user.address.country);
userMap.put("address", addressMap);

ctx.add("user", userMap);
```

<a name="variable-scope"></a>
## Variable Scope

### Global Scope

Variables added to the context are available throughout the template:

```java
ctx.add("siteName", "My Website");
```

**Template:**
```html
<header>{{ siteName }}</header>
<main>{{ siteName }}</main>
<footer>{{ siteName }}</footer>
```

### Loop Scope

Loop variables are scoped to the loop:

```html
{% for (i = 0; i < 5; i++) %}
    {{ i }}  <!-- i is available here -->
{% endfor %}
{{ i }}  <!-- i is NOT available here -->
```

### Include Scope

Included templates have access to the parent context:

**main.jhp:**
```html
{% include 'header.jhp' %}
```

**header.jhp:**
```html
<h1>{{ title }}</h1>  <!-- Can access title from main context -->
```

<a name="best-practices"></a>
## Best Practices

### 1. Use Descriptive Names

```java
// Good
ctx.add("userName", "Alice");
ctx.add("productList", products);

// Avoid
ctx.add("x", "Alice");
ctx.add("data", products);
```

### 2. Prefer Maps Over POJOs for Complex Data

```java
// Better for templates
Map<String, Object> user = new HashMap<>();
user.put("name", "Alice");
user.put("profile", profileMap);
ctx.add("user", user);
```

### 3. Prepare Data in Java, Not Templates

```java
// Good - calculate in Java
int total = items.stream().mapToInt(Item::getPrice).sum();
ctx.add("total", total);

// Avoid - complex logic in template
ctx.add("items", items);
// Then doing: {{ items[0].price + items[1].price + ... }}
```

### 4. Use Null-Safe Defaults

```java
ctx.add("title", title != null ? title : "Untitled");
ctx.add("items", items != null ? items : Collections.emptyList());
```

### 5. Group Related Data

```java
Map<String, Object> page = new HashMap<>();
page.put("title", "Home");
page.put("description", "Welcome");
page.put("keywords", Arrays.asList("home", "welcome"));
ctx.add("page", page);
```

**Template:**
```html
<title>{{ page.title }}</title>
<meta name="description" content="{{ page.description }}">
```

### 6. Reuse Context Objects

```java
// Create a base context with common variables
Context baseContext = new Context();
baseContext.add("siteName", "My Site");
baseContext.add("year", 2025);

// For each page, create a new context with page-specific data
Context pageContext = new Context();
// Copy base context data
pageContext.getContext().putAll(baseContext.getContext());
pageContext.add("title", "Page Title");
```

## Getting Context Data

Access the underlying map if needed:

```java
Context ctx = new Context();
ctx.add("name", "Alice");

Map<String, Object> data = ctx.getContext();
System.out.println(data.get("name")); // Alice
```

## Immutability Considerations

The Context class doesn't enforce immutability. Variables can be modified during rendering (e.g., in loops). If you need immutable data, consider using immutable collections:

```java
List<String> items = List.of("A", "B", "C");  // Immutable list
ctx.add("items", items);
```

## Next Steps

- Learn about [Configuration](configuration.md)
- Explore [Advanced Usage](advanced-usage.md)
- Review [API Reference](api-reference.md)
