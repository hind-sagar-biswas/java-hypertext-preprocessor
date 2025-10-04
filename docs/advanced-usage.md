# Advanced Usage

- [Introduction](#introduction)
- [Template Caching](#template-caching)
- [Thread Safety](#thread-safety)
- [Custom Function Library](#custom-function-library)
- [Error Handling Strategies](#error-handling-strategies)
- [Performance Optimization](#performance-optimization)
- [Integration Patterns](#integration-patterns)

<a name="introduction"></a>
## Introduction

This guide covers advanced topics for getting the most out of JHP in production environments.

<a name="template-caching"></a>
## Template Caching

JHP automatically caches parsed templates (AST) to improve performance.

### How Caching Works

1. Template is parsed into an Abstract Syntax Tree (AST)
2. AST is cached using the normalized file path as key
3. Subsequent renders use the cached AST
4. No need to parse the template again

### Cache Behavior

```java
JhpEngine engine = new JhpEngine(settings, lib);

// First render - parses and caches
String output1 = engine.render("page.jhp", ctx1);

// Second render - uses cached AST
String output2 = engine.render("page.jhp", ctx2);
```

### Cache Invalidation

The cache is per-engine instance. To invalidate:

```java
// Create a new engine instance
JhpEngine newEngine = new JhpEngine(settings, lib);
```

### Cache Key

Templates are cached by their absolute normalized path:

```java
// These use the same cache entry:
engine.render("page.jhp", ctx);
engine.render("./page.jhp", ctx);
engine.render("/full/path/to/page.jhp", ctx);
```

### Memory Considerations

Each engine instance maintains its own cache. For applications with many templates:

```java
// Single engine for entire application
public class TemplateService {
    private static final JhpEngine engine = createEngine();
    
    public static String render(String template, Context ctx) throws Exception {
        return engine.render(template, ctx);
    }
}
```

<a name="thread-safety"></a>
## Thread Safety

JHP is designed to be thread-safe for concurrent rendering.

### Thread-Safe Components

- **JhpEngine** - Safe to share across threads
- **FunctionLibrary** - Uses ConcurrentHashMap for custom functions
- **Settings** - Immutable after creation

### Thread-Local State

- **Include Stack** - Uses ThreadLocal to track includes per thread
- **Scope Stack** - Each render has its own scope stack

### Concurrent Rendering

```java
JhpEngine engine = new JhpEngine(settings, lib);

// Safe to use from multiple threads
ExecutorService executor = Executors.newFixedThreadPool(10);

for (int i = 0; i < 100; i++) {
    final int userId = i;
    executor.submit(() -> {
        try {
            Context ctx = new Context();
            ctx.add("userId", userId);
            String output = engine.render("user-profile.jhp", ctx);
            // Process output
        } catch (Exception e) {
            e.printStackTrace();
        }
    });
}
```

### Context is NOT Thread-Safe

Each thread should create its own Context:

```java
// WRONG - Don't share Context across threads
Context sharedCtx = new Context();
executor.submit(() -> engine.render("page.jhp", sharedCtx)); // ❌

// RIGHT - Create Context per thread
executor.submit(() -> {
    Context ctx = new Context();
    ctx.add("data", data);
    engine.render("page.jhp", ctx); // ✓
});
```

<a name="custom-function-library"></a>
## Custom Function Library

Create reusable function libraries for your application.

### Creating a Custom Library

```java
public class MyFunctionLibrary extends FunctionLibrary {
    
    public MyFunctionLibrary() {
        super();
        registerCustomFunctions();
    }
    
    private void registerCustomFunctions() {
        // Date formatting
        register("formatdate", (args, scopes) -> {
            if (args.isEmpty()) return "";
            Instant instant = (Instant) args.get(0);
            DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
            return formatter.format(instant);
        });
        
        // Currency formatting
        register("currency", (args, scopes) -> {
            if (args.isEmpty()) return "$0.00";
            Number amount = (Number) args.get(0);
            return String.format("$%.2f", amount.doubleValue());
        });
        
        // Truncate string
        register("truncate", (args, scopes) -> {
            if (args.size() < 2) return "";
            String text = args.get(0).toString();
            int length = ((Number) args.get(1)).intValue();
            if (text.length() <= length) return text;
            return text.substring(0, length) + "...";
        });
    }
}
```

### Using Custom Library

```java
MyFunctionLibrary lib = new MyFunctionLibrary();
JhpEngine engine = new JhpEngine(settings, lib);
```

### Domain-Specific Functions

```java
public class EcommerceFunctions extends FunctionLibrary {
    
    public EcommerceFunctions() {
        super();
        
        // Calculate discount
        register("discount", (args, scopes) -> {
            Number price = (Number) args.get(0);
            Number percent = (Number) args.get(1);
            double discounted = price.doubleValue() * (1 - percent.doubleValue() / 100);
            return String.format("%.2f", discounted);
        });
        
        // Format SKU
        register("formatsku", (args, scopes) -> {
            String sku = args.get(0).toString();
            return sku.toUpperCase().replaceAll("[^A-Z0-9]", "");
        });
    }
}
```

<a name="error-handling-strategies"></a>
## Error Handling Strategies

### Strategy 1: Fail Fast (Development)

```java
Settings settings = Settings.builder()
    .issueHandleMode(IssueHandleMode.THROW)
    .build();

try {
    String output = engine.render("template.jhp", ctx);
} catch (Exception e) {
    logger.error("Template error: {}", e.getMessage());
    throw e; // Propagate to caller
}
```

### Strategy 2: Graceful Degradation (Production)

```java
Settings settings = Settings.builder()
    .issueHandleMode(IssueHandleMode.COMMENT)
    .build();

try {
    String output = engine.render("template.jhp", ctx);
    return output;
} catch (Exception e) {
    logger.error("Template rendering failed", e);
    return renderErrorPage(e);
}
```

### Strategy 3: Fallback Templates

```java
public String renderWithFallback(String template, Context ctx) {
    try {
        return engine.render(template, ctx);
    } catch (Exception e) {
        logger.warn("Primary template failed, using fallback", e);
        try {
            return engine.render("fallback.jhp", ctx);
        } catch (Exception fallbackError) {
            logger.error("Fallback template also failed", fallbackError);
            return "<h1>Error</h1><p>Unable to render page</p>";
        }
    }
}
```

### Strategy 4: Partial Rendering

```java
public String renderSafely(List<String> sections, Context ctx) {
    StringBuilder result = new StringBuilder();
    
    for (String section : sections) {
        try {
            result.append(engine.render(section + ".jhp", ctx));
        } catch (Exception e) {
            logger.error("Section {} failed", section, e);
            result.append("<!-- Section ").append(section).append(" failed -->");
        }
    }
    
    return result.toString();
}
```

<a name="performance-optimization"></a>
## Performance Optimization

### 1. Reuse Engine Instances

```java
// SLOW - Creates new engine for each request
public String render(String template, Context ctx) {
    JhpEngine engine = new JhpEngine(settings, lib);
    return engine.render(template, ctx);
}

// FAST - Reuse engine instance
private static final JhpEngine engine = new JhpEngine(settings, lib);

public String render(String template, Context ctx) {
    return engine.render(template, ctx);
}
```

### 2. Prepare Context Efficiently

```java
// SLOW - Multiple small operations
Context ctx = new Context();
ctx.add("var1", value1);
ctx.add("var2", value2);
// ... many more

// FASTER - Batch preparation
Map<String, Object> data = new HashMap<>();
data.put("var1", value1);
data.put("var2", value2);
// ... prepare all data
Context ctx = new Context();
ctx.getContext().putAll(data);
```

### 3. Minimize Include Depth

```java
// SLOW - Deep nesting
main.jhp → layout.jhp → header.jhp → nav.jhp → menu.jhp

// FASTER - Flatter structure
main.jhp → layout.jhp → header.jhp
```

### 4. Use Appropriate Data Structures

```java
// SLOW - Converting large POJOs
LargeObject obj = new LargeObject();
ctx.add("data", obj); // Converts all public fields

// FASTER - Use Map with only needed data
Map<String, Object> data = new HashMap<>();
data.put("id", obj.getId());
data.put("name", obj.getName());
ctx.add("data", data);
```

### 5. Benchmark Your Templates

```java
public void benchmarkTemplate() {
    long start = System.nanoTime();
    
    for (int i = 0; i < 1000; i++) {
        Context ctx = new Context();
        ctx.add("index", i);
        engine.render("template.jhp", ctx);
    }
    
    long duration = System.nanoTime() - start;
    System.out.printf("Average: %.2f ms%n", duration / 1_000_000.0 / 1000);
}
```

<a name="integration-patterns"></a>
## Integration Patterns

### Web Framework Integration

```java
@Controller
public class PageController {
    private final JhpEngine engine;
    
    public PageController() {
        Settings settings = Settings.builder()
            .base("src/main/resources/templates")
            .build();
        FunctionLibrary lib = new FunctionLibrary();
        this.engine = new JhpEngine(settings, lib);
    }
    
    @GetMapping("/page/{id}")
    public ResponseEntity<String> getPage(@PathVariable String id) {
        try {
            Context ctx = new Context();
            ctx.add("pageId", id);
            ctx.add("title", "Page " + id);
            
            String html = engine.render("page.jhp", ctx);
            return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error rendering page");
        }
    }
}
```

### Email Template System

```java
public class EmailService {
    private final JhpEngine engine;
    
    public EmailService() {
        Settings settings = Settings.builder()
            .base("email-templates")
            .escape(false) // Emails might need raw HTML
            .build();
        this.engine = new JhpEngine(settings, new FunctionLibrary());
    }
    
    public void sendWelcomeEmail(User user) throws Exception {
        Context ctx = new Context();
        ctx.add("userName", user.getName());
        ctx.add("activationLink", generateActivationLink(user));
        
        String htmlBody = engine.render("welcome-email.jhp", ctx);
        String textBody = engine.render("welcome-email-text.jhp", ctx);
        
        emailClient.send(user.getEmail(), "Welcome!", htmlBody, textBody);
    }
}
```

### Report Generation

```java
public class ReportGenerator {
    private final JhpEngine engine;
    
    public void generateReport(ReportData data, OutputStream output) throws Exception {
        Context ctx = new Context();
        ctx.add("reportDate", Instant.now());
        ctx.add("data", data.toMap());
        ctx.add("summary", data.getSummary());
        
        String html = engine.render("report-template.jhp", ctx);
        
        // Convert HTML to PDF or write directly
        output.write(html.getBytes(StandardCharsets.UTF_8));
    }
}
```

### Static Site Generator

```java
public class StaticSiteGenerator {
    private final JhpEngine engine;
    private final Path outputDir;
    
    public void generateSite(List<Page> pages) throws Exception {
        for (Page page : pages) {
            Context ctx = new Context();
            ctx.add("page", page.toMap());
            ctx.add("siteName", "My Site");
            
            String html = engine.render("page-layout.jhp", ctx);
            
            Path outputFile = outputDir.resolve(page.getSlug() + ".html");
            Files.writeString(outputFile, html);
        }
    }
}
```

## Next Steps

- Review [API Reference](api-reference.md)
- Check out [Examples](examples.md)
- Explore the source code for deeper understanding
