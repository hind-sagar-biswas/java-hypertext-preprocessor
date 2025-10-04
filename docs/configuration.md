# Configuration

- [Introduction](#introduction)
- [Settings Builder](#settings-builder)
- [Base Directory](#base-directory)
- [HTML Escaping](#html-escaping)
- [Include Depth](#include-depth)
- [Issue Handling](#issue-handling)
- [Complete Configuration Example](#complete-configuration-example)

<a name="introduction"></a>
## Introduction

JHP is configured using the `Settings` class, which uses a builder pattern for easy and readable configuration.

<a name="settings-builder"></a>
## Settings Builder

Create settings using the builder:

```java
Settings settings = Settings.builder()
    .base("/path/to/templates")
    .escape(true)
    .maxIncludeDepth(15)
    .issueHandleMode(IssueHandleMode.COMMENT)
    .build();
```

<a name="base-directory"></a>
## Base Directory

The base directory is the root path for all template files.

### Setting Base Directory

```java
Settings settings = Settings.builder()
    .base("/var/www/templates")
    .build();
```

### Relative Paths

```java
Settings settings = Settings.builder()
    .base("src/main/resources/templates")
    .build();
```

### Default Base Directory

If not specified, the base directory defaults to the current directory (`.`):

```java
Settings settings = Settings.builder().build();
// base = "."
```

### Security Implications

The base directory acts as a security boundary. Templates cannot include files outside this directory:

```java
Settings settings = Settings.builder()
    .base("/var/www/templates")
    .build();
```

**Template:**
```html
{% include '../../../etc/passwd' %}
<!-- Error: Path not in base directory -->
```

<a name="html-escaping"></a>
## HTML Escaping

Control whether output is HTML-escaped by default.

### Enable Escaping (Default)

```java
Settings settings = Settings.builder()
    .escape(true)  // Default
    .build();
```

**Template:**
```html
{{ userInput }}
<!-- <script> becomes &lt;script&gt; -->
```

### Disable Escaping

```java
Settings settings = Settings.builder()
    .escape(false)
    .build();
```

**Template:**
```html
{{ htmlContent }}
<!-- HTML is rendered as-is -->
```

### Default Value

The default escaping mode is `true`:

```java
System.out.println(Settings.defaultEscapeMode); // true
```

### Override in Templates

Even with escaping enabled, you can output raw HTML using triple braces:

```html
{{ escaped }}      <!-- HTML-escaped -->
{{{ notEscaped }}} <!-- Raw HTML -->
```

<a name="include-depth"></a>
## Include Depth

Limit the maximum depth of nested includes to prevent infinite recursion.

### Setting Max Depth

```java
Settings settings = Settings.builder()
    .maxIncludeDepth(10)
    .build();
```

### Default Value

The default maximum include depth is 15:

```java
System.out.println(Settings.defaultMaxIncludeDepth); // 15
```

### Why Limit Depth?

Limiting include depth prevents:
- Infinite include loops
- Stack overflow errors
- Performance issues

### Example

```java
Settings settings = Settings.builder()
    .maxIncludeDepth(3)
    .build();
```

**Template Structure:**
```
main.jhp
  └─ includes header.jhp (depth 1)
      └─ includes logo.jhp (depth 2)
          └─ includes image.jhp (depth 3)
              └─ includes watermark.jhp (depth 4) ❌ Error!
```

<a name="issue-handling"></a>
## Issue Handling

Configure how JHP handles runtime issues like missing variables or include errors.

### Issue Handle Modes

JHP provides four modes for handling issues:

#### COMMENT (Default)

Renders issues as HTML comments:

```java
Settings settings = Settings.builder()
    .issueHandleMode(IssueHandleMode.COMMENT)
    .build();
```

**Output:**
```html
<!-- MISSING_VARIABLE: Variable 'username' not found -->
```

#### THROW

Throws exceptions for issues:

```java
Settings settings = Settings.builder()
    .issueHandleMode(IssueHandleMode.THROW)
    .build();
```

**Result:**
```java
// Throws RuntimeException
```

Use this mode in development or when you want strict error handling.

#### DEBUG

Renders detailed debug information:

```java
Settings settings = Settings.builder()
    .issueHandleMode(IssueHandleMode.DEBUG)
    .build();
```

**Output:**
```html
<details style="...">
    <summary>Template Engine Debug — Issue: MISSING_VARIABLE</summary>
    <table>
        <tr><td>Issue Type</td><td>MISSING_VARIABLE</td></tr>
        <tr><td>Message</td><td>Variable 'username' not found</td></tr>
        <tr><td>Timestamp</td><td>2025-10-05T00:18:03Z</td></tr>
        <tr><td>Thread</td><td>main</td></tr>
        <tr><td>Include Stack</td><td>...</td></tr>
    </table>
</details>
```

Perfect for development and debugging.

#### IGNORE

Silently ignores issues:

```java
Settings settings = Settings.builder()
    .issueHandleMode(IssueHandleMode.IGNORE)
    .build();
```

**Output:**
```html
<!-- Nothing rendered for errors -->
```

Use with caution - errors are completely silent.

### Default Value

The default issue handle mode is `COMMENT`:

```java
System.out.println(Settings.defaultIssueHandleMode); // COMMENT
```

### Issue Types

JHP can handle various issue types:

- `MISSING_VARIABLE` - Variable not found in context
- `FUNCTION_ERROR` - Function execution error
- `INCLUDE_ERROR` - Include file error
- `INCLUDE_CYCLE` - Circular include detected
- `INCLUDE_MAX_DEPTH` - Max include depth exceeded
- `INCLUDE_NOT_IN_BASE_DIR` - Include path outside base directory
- `MISSING_INCLUDE` - Include file not found
- `UNKNOWN_ELEMENT` - Unknown template element
- `FUNCTION_CALL_ERROR` - Invalid function call

### Recommended Modes by Environment

**Development:**
```java
.issueHandleMode(IssueHandleMode.DEBUG)
```

**Testing:**
```java
.issueHandleMode(IssueHandleMode.THROW)
```

**Production:**
```java
.issueHandleMode(IssueHandleMode.COMMENT)
// or
.issueHandleMode(IssueHandleMode.IGNORE)
```

<a name="complete-configuration-example"></a>
## Complete Configuration Example

### Development Configuration

```java
Settings devSettings = Settings.builder()
    .base("src/main/resources/templates")
    .escape(true)
    .maxIncludeDepth(20)
    .issueHandleMode(IssueHandleMode.DEBUG)
    .build();

FunctionLibrary lib = new FunctionLibrary();
JhpEngine devEngine = new JhpEngine(devSettings, lib);
```

### Production Configuration

```java
Settings prodSettings = Settings.builder()
    .base("/var/www/app/templates")
    .escape(true)
    .maxIncludeDepth(10)
    .issueHandleMode(IssueHandleMode.COMMENT)
    .build();

FunctionLibrary lib = new FunctionLibrary();
JhpEngine prodEngine = new JhpEngine(prodSettings, lib);
```

### Testing Configuration

```java
Settings testSettings = Settings.builder()
    .base("src/test/resources/templates")
    .escape(true)
    .maxIncludeDepth(5)
    .issueHandleMode(IssueHandleMode.THROW)
    .build();

FunctionLibrary lib = new FunctionLibrary();
JhpEngine testEngine = new JhpEngine(testSettings, lib);
```

## Configuration Best Practices

### 1. Use Environment-Specific Settings

```java
public class TemplateConfig {
    public static Settings getSettings() {
        String env = System.getenv("APP_ENV");
        
        if ("production".equals(env)) {
            return productionSettings();
        } else if ("test".equals(env)) {
            return testSettings();
        } else {
            return developmentSettings();
        }
    }
    
    private static Settings productionSettings() {
        return Settings.builder()
            .base("/var/www/templates")
            .escape(true)
            .issueHandleMode(IssueHandleMode.COMMENT)
            .build();
    }
    
    private static Settings developmentSettings() {
        return Settings.builder()
            .base("src/main/resources/templates")
            .escape(true)
            .issueHandleMode(IssueHandleMode.DEBUG)
            .build();
    }
    
    private static Settings testSettings() {
        return Settings.builder()
            .base("src/test/resources/templates")
            .escape(true)
            .issueHandleMode(IssueHandleMode.THROW)
            .build();
    }
}
```

### 2. Centralize Engine Creation

```java
public class TemplateEngine {
    private static final JhpEngine instance;
    
    static {
        Settings settings = TemplateConfig.getSettings();
        FunctionLibrary lib = new FunctionLibrary();
        // Register custom functions
        registerCustomFunctions(lib);
        instance = new JhpEngine(settings, lib);
    }
    
    public static JhpEngine getInstance() {
        return instance;
    }
    
    private static void registerCustomFunctions(FunctionLibrary lib) {
        // Register your custom functions here
    }
}
```

### 3. Always Enable Escaping

Unless you have a specific reason, always keep HTML escaping enabled:

```java
.escape(true)  // Prevent XSS attacks
```

### 4. Set Reasonable Include Depth

```java
.maxIncludeDepth(10)  // Enough for most use cases
```

## Immutability

Settings objects are immutable after creation. To change settings, create a new Settings instance:

```java
Settings settings1 = Settings.builder().base("/path1").build();
Settings settings2 = Settings.builder().base("/path2").build();

JhpEngine engine1 = new JhpEngine(settings1, lib);
JhpEngine engine2 = new JhpEngine(settings2, lib);
```

## Next Steps

- Explore [Advanced Usage](advanced-usage.md)
- Review [API Reference](api-reference.md)
- Check out [Examples](examples.md)
