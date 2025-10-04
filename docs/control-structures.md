# Control Structures

- [Introduction](#introduction)
- [If Statements](#if-statements)
- [For Loops](#for-loops)
- [Foreach Loops](#foreach-loops)
- [While Loops](#while-loops)
- [Break and Continue](#break-and-continue)
- [Template Includes](#template-includes)

<a name="introduction"></a>
## Introduction

Control structures allow you to add logic to your templates. They use the `{% %}` syntax and support common programming constructs like conditionals and loops.

<a name="if-statements"></a>
## If Statements

### Basic If

```html
{% if (age >= 18) %}
    <p>You are an adult.</p>
{% endif %}
```

### If-Else

```html
{% if (isLoggedIn) %}
    <p>Welcome back, {{ username }}!</p>
{% else %}
    <p>Please log in.</p>
{% endif %}
```

### If-ElseIf-Else

```html
{% if (score >= 90) %}
    <span class="grade-a">Grade: A</span>
{% elseif (score >= 80) %}
    <span class="grade-b">Grade: B</span>
{% elseif (score >= 70) %}
    <span class="grade-c">Grade: C</span>
{% else %}
    <span class="grade-f">Grade: F</span>
{% endif %}
```

### Complex Conditions

```html
{% if (user.age >= 18 && user.verified) %}
    <p>Full access granted.</p>
{% endif %}

{% if (isAdmin || isModerator) %}
    <button>Manage Content</button>
{% endif %}

{% if (!isDeleted && (isPublished || isDraft)) %}
    <article>{{ content }}</article>
{% endif %}
```

### Nested If Statements

```html
{% if (user) %}
    {% if (user.isPremium) %}
        <div class="premium-content">
            {{ premiumContent }}
        </div>
    {% else %}
        <div class="free-content">
            {{ freeContent }}
        </div>
    {% endif %}
{% else %}
    <p>Please log in to view content.</p>
{% endif %}
```

<a name="for-loops"></a>
## For Loops

The `for` loop provides traditional C-style iteration with initialization, condition, and update.

### Basic For Loop

```html
{% for (i = 0; i < 5; i++) %}
    <p>Item {{ i }}</p>
{% endfor %}
```

**Output:**
```html
<p>Item 0</p>
<p>Item 1</p>
<p>Item 2</p>
<p>Item 3</p>
<p>Item 4</p>
```

### Counting Down

```html
{% for (i = 10; i > 0; i--) %}
    <div>{{ i }}</div>
{% endfor %}
```

### Custom Increment

```html
{% for (i = 0; i < 100; i = i + 10) %}
    <span>{{ i }}</span>
{% endfor %}
```

### Using Loop Variable

```html
<ul>
{% for (i = 0; i < items.length; i++) %}
    <li>{{ i + 1 }}. {{ items[i] }}</li>
{% endfor %}
</ul>
```

<a name="foreach-loops"></a>
## Foreach Loops

The `foreach` loop iterates over collections, arrays, and maps.

### Iterating Over Lists

```html
<ul>
{% foreach (fruit in fruits) %}
    <li>{{ fruit }}</li>
{% endforeach %}
</ul>
```

**Java Context:**
```java
List<String> fruits = Arrays.asList("Apple", "Banana", "Cherry");
ctx.add("fruits", fruits);
```

### Iterating Over Arrays

```html
{% foreach (item in items) %}
    <div>{{ item }}</div>
{% endforeach %}
```

### Iterating Over Maps (with Key)

```html
<dl>
{% foreach (key, value in config) %}
    <dt>{{ key }}</dt>
    <dd>{{ value }}</dd>
{% endforeach %}
</dl>
```

**Java Context:**
```java
Map<String, String> config = new HashMap<>();
config.put("theme", "dark");
config.put("language", "en");
ctx.add("config", config);
```

### Nested Foreach

```html
{% foreach (category in categories) %}
    <h2>{{ category.name }}</h2>
    <ul>
    {% foreach (product in category.products) %}
        <li>{{ product.name }} - ${{ product.price }}</li>
    {% endforeach %}
    </ul>
{% endforeach %}
```

### Empty Collections

If a collection is empty or null, the foreach body won't execute:

```html
{% foreach (item in emptyList) %}
    <p>This won't be displayed</p>
{% endforeach %}
```

<a name="while-loops"></a>
## While Loops

While loops execute as long as a condition is true.

### Basic While Loop

```html
{% while (count < 5) %}
    <p>Count: {{ count }}</p>
    {% count = count + 1 %}
{% endwhile %}
```

> **Note:** Be careful with while loops to avoid infinite loops. Ensure the condition will eventually become false.

### While with Complex Condition

```html
{% while (hasMore && index < maxItems) %}
    <div>{{ items[index] }}</div>
    {% index = index + 1 %}
{% endwhile %}
```

<a name="break-and-continue"></a>
## Break and Continue

Control loop execution with `break` and `continue` statements.

### Break Statement

Exit a loop early:

```html
{% for (i = 0; i < 100; i++) %}
    {% if (i == 10) %}
        {% break %}
    {% endif %}
    <p>{{ i }}</p>
{% endfor %}
<!-- Stops at 9 -->
```

```html
{% foreach (item in items) %}
    {% if (item == "STOP") %}
        {% break %}
    {% endif %}
    <li>{{ item }}</li>
{% endforeach %}
```

### Continue Statement

Skip to the next iteration:

```html
{% for (i = 0; i < 10; i++) %}
    {% if (i % 2 == 0) %}
        {% continue %}
    {% endif %}
    <p>Odd number: {{ i }}</p>
{% endfor %}
<!-- Only displays odd numbers -->
```

```html
{% foreach (user in users) %}
    {% if (!user.active) %}
        {% continue %}
    {% endif %}
    <div class="user">{{ user.name }}</div>
{% endforeach %}
```

<a name="template-includes"></a>
## Template Includes

Include other templates to create modular, reusable components.

### Basic Include

```html
{% include 'header.jhp' %}

<main>
    <h1>{{ title }}</h1>
    <p>{{ content }}</p>
</main>

{% include 'footer.jhp' %}
```

### Include with Relative Paths

```html
{% include 'partials/navigation.jhp' %}
{% include '../shared/sidebar.jhp' %}
```

### Nested Includes

**layout.jhp:**
```html
<!DOCTYPE html>
<html>
<head>
    {% include 'partials/head.jhp' %}
</head>
<body>
    {% include 'partials/header.jhp' %}
    <main>
        <!-- Content here -->
    </main>
    {% include 'partials/footer.jhp' %}
</body>
</html>
```

### Include Security

JHP enforces security by:
- Restricting includes to the base directory (if configured)
- Detecting circular includes
- Limiting include depth (default: 15 levels)

**Circular Include Detection:**
```html
<!-- page1.jhp -->
{% include 'page2.jhp' %}

<!-- page2.jhp -->
{% include 'page1.jhp' %}  <!-- Error: Circular include detected -->
```

## Combining Control Structures

You can combine different control structures:

```html
{% if (categories) %}
    {% foreach (category in categories) %}
        <section>
            <h2>{{ category.name }}</h2>
            {% if (category.items) %}
                <ul>
                {% for (i = 0; i < category.items.length && i < 10; i++) %}
                    <li>{{ category.items[i].name }}</li>
                {% endfor %}
                </ul>
            {% else %}
                <p>No items in this category.</p>
            {% endif %}
        </section>
    {% endforeach %}
{% else %}
    <p>No categories available.</p>
{% endif %}
```

## Best Practices

1. **Keep Logic Simple** - Complex logic belongs in Java code, not templates
2. **Avoid Deep Nesting** - Extract complex structures into separate templates
3. **Use Meaningful Variable Names** - Make templates readable
4. **Watch for Infinite Loops** - Always ensure loop conditions will terminate
5. **Prefer Foreach Over For** - When iterating collections, foreach is cleaner

## Next Steps

- Learn about [Functions](functions.md)
- Explore [Context & Variables](context-variables.md)
- Review [Configuration](configuration.md) options
