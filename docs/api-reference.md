# API Reference

- [Core Classes](#core-classes)
- [JhpEngine](#jhpengine)
- [Context](#context)
- [Settings](#settings)
- [FunctionLibrary](#functionlibrary)
- [Enums](#enums)
- [Exceptions](#exceptions)

<a name="core-classes"></a>
## Core Classes

The JHP API consists of several core classes that work together to provide template rendering functionality.

<a name="jhpengine"></a>
## JhpEngine

The main class for rendering templates.

### Package

```java
com.hindbiswas.jhp.engine.JhpEngine
```

### Constructor

```java
public JhpEngine(Settings settings, FunctionLibraryContext functionLibrary)
```

**Parameters:**
- `settings` - Configuration settings for the engine
- `functionLibrary` - Function library instance (typically `FunctionLibrary`)

**Example:**
```java
Settings settings = Settings.builder().base("/templates").build();
FunctionLibrary lib = new FunctionLibrary();
JhpEngine engine = new JhpEngine(settings, lib);
```

### Methods

#### render(Path, Context)

```java
public String render(Path templatePath, Context context) throws Exception
```

Renders a template using a Path object.

**Parameters:**
- `templatePath` - Absolute path to the template file
- `context` - Context containing variables

**Returns:** Rendered template as String

**Throws:** Exception if template parsing or rendering fails

**Example:**
```java
Path path = Path.of("/templates/page.jhp");
Context ctx = new Context();
ctx.add("title", "My Page");
String output = engine.render(path, ctx);
```

#### render(String, Context)

```java
public String render(String pathTxt, Context context) throws Exception
```

Renders a template using a string path (relative to base directory).

**Parameters:**
- `pathTxt` - Path to template (relative to base directory)
- `context` - Context containing variables

**Returns:** Rendered template as String

**Throws:** Exception if template parsing or rendering fails

**Example:**
```java
Context ctx = new Context();
ctx.add("title", "My Page");
String output = engine.render("page.jhp", ctx);
```

### Thread Safety

`JhpEngine` is thread-safe and can be shared across multiple threads.

---

<a name="context"></a>
## Context

Holds variables that will be available in templates.

### Package

```java
com.hindbiswas.jhp.Context
```

### Constructor

```java
public Context()
```

Creates a new empty context.

**Example:**
```java
Context ctx = new Context();
```

### Methods

#### add(String, Object)

```java
public void add(String key, Object value)
```

Adds a variable to the context.

**Parameters:**
- `key` - Variable name
- `value` - Variable value (any Object)

**Example:**
```java
ctx.add("name", "Alice");
ctx.add("age", 25);
ctx.add("items", Arrays.asList("A", "B", "C"));
```

#### getContext()

```java
public Map<String, Object> getContext()
```

Returns the underlying map of context variables.

**Returns:** Map containing all context variables

**Example:**
```java
Map<String, Object> data = ctx.getContext();
System.out.println(data.get("name"));
```

### Value Conversion

Context automatically converts values:
- **Primitives and Strings** - Stored as-is
- **Collections** - Converted to List
- **Arrays** - Converted to Object[]
- **Maps** - Stored as-is
- **POJOs** - Converted to Map (public fields only)

---

<a name="settings"></a>
## Settings

Configuration for the JHP engine.

### Package

```java
com.hindbiswas.jhp.engine.Settings
```

### Static Methods

#### builder()

```java
public static SettingsBuilder builder()
```

Creates a new settings builder.

**Returns:** SettingsBuilder instance

**Example:**
```java
Settings settings = Settings.builder()
    .base("/templates")
    .build();
```

### Fields

#### base

```java
public final Path base
```

Base directory for templates.

#### escapeByDefault

```java
public final boolean escapeByDefault
```

Whether to HTML-escape output by default.

#### maxIncludeDepth

```java
public final int maxIncludeDepth
```

Maximum depth for nested includes.

#### issueHandleMode

```java
public final IssueHandleMode issueHandleMode
```

How to handle runtime issues.

### Static Fields

#### defaultMaxIncludeDepth

```java
public static int defaultMaxIncludeDepth = 15
```

Default maximum include depth.

#### defaultEscapeMode

```java
public static boolean defaultEscapeMode = true
```

Default HTML escaping mode.

#### defaultIssueHandleMode

```java
public static IssueHandleMode defaultIssueHandleMode = IssueHandleMode.COMMENT
```

Default issue handling mode.

---

## SettingsBuilder

Builder for creating Settings instances.

### Package

```java
com.hindbiswas.jhp.engine.Settings.SettingsBuilder
```

### Methods

#### base(String)

```java
public SettingsBuilder base(String base)
```

Sets the base directory.

**Parameters:**
- `base` - Path to base directory

**Returns:** This builder

#### escape(boolean)

```java
public SettingsBuilder escape(boolean escapeByDefault)
```

Sets HTML escaping mode.

**Parameters:**
- `escapeByDefault` - true to enable escaping

**Returns:** This builder

#### maxIncludeDepth(int)

```java
public SettingsBuilder maxIncludeDepth(int maxIncludeDepth)
```

Sets maximum include depth.

**Parameters:**
- `maxIncludeDepth` - Maximum depth

**Returns:** This builder

#### issueHandleMode(IssueHandleMode)

```java
public SettingsBuilder issueHandleMode(IssueHandleMode issueHandleMode)
```

Sets issue handling mode.

**Parameters:**
- `issueHandleMode` - Issue handling mode

**Returns:** This builder

#### build()

```java
public Settings build()
```

Builds the Settings instance.

**Returns:** Immutable Settings object

---

<a name="functionlibrary"></a>
## FunctionLibrary

Manages built-in and custom functions.

### Package

```java
com.hindbiswas.jhp.engine.FunctionLibrary
```

### Constructor

```java
public FunctionLibrary()
```

Creates a new function library with built-in functions.

### Methods

#### register(String, JhpFunction)

```java
public JhpFunction register(String name, JhpFunction fn) throws IllegalArgumentException
```

Registers a custom function.

**Parameters:**
- `name` - Function name (case-insensitive)
- `fn` - Function implementation

**Returns:** Previously registered function with same name, or null

**Throws:** IllegalArgumentException if name or function is null/empty

**Example:**
```java
lib.register("double", (args, scopes) -> {
    Number n = (Number) args.get(0);
    return n.doubleValue() * 2;
});
```

#### unregister(String)

```java
public JhpFunction unregister(String name)
```

Unregisters a custom function.

**Parameters:**
- `name` - Function name

**Returns:** Unregistered function, or null

#### hasFunction(String)

```java
public boolean hasFunction(String name)
```

Checks if a function exists.

**Parameters:**
- `name` - Function name

**Returns:** true if function exists

#### callFunction(String, List<Object>, Deque<Map<String, Object>>)

```java
public Object callFunction(String name, List<Object> args, Deque<Map<String, Object>> scopes)
```

Calls a function (used internally by the engine).

**Parameters:**
- `name` - Function name
- `args` - Function arguments
- `scopes` - Variable scopes

**Returns:** Function result

**Throws:** IllegalArgumentException if function not found

---

## JhpFunction

Functional interface for custom functions.

### Package

```java
com.hindbiswas.jhp.engine.FunctionLibrary.JhpFunction
```

### Method

```java
Object apply(List<Object> args, Deque<Map<String, Object>> scopes)
```

**Parameters:**
- `args` - List of arguments passed to function
- `scopes` - Stack of variable scopes (for accessing context)

**Returns:** Function result (any Object)

**Example:**
```java
JhpFunction myFunc = (args, scopes) -> {
    if (args.isEmpty()) return "";
    return args.get(0).toString().toUpperCase();
};
```

---

<a name="enums"></a>
## Enums

### IssueHandleMode

How the engine handles runtime issues.

**Package:** `com.hindbiswas.jhp.engine.IssueHandleMode`

**Values:**
- `DEBUG` - Render detailed debug information
- `THROW` - Throw exceptions
- `COMMENT` - Render as HTML comments
- `IGNORE` - Silently ignore

**Example:**
```java
Settings settings = Settings.builder()
    .issueHandleMode(IssueHandleMode.THROW)
    .build();
```

### IssueType

Types of runtime issues.

**Package:** `com.hindbiswas.jhp.engine.IssueType`

**Values:**
- `INCLUDE_CYCLE` - Circular include detected
- `MISSING_VARIABLE` - Variable not found
- `FUNCTION_ERROR` - Function execution error
- `EXPRESSION_ERROR` - Expression evaluation error
- `MISSING_INCLUDE` - Include file not found
- `INCLUDE_NOT_IN_BASE_DIR` - Include outside base directory
- `INCLUDE_MAX_DEPTH` - Max include depth exceeded
- `INCLUDE_ERROR` - General include error
- `UNKNOWN_ELEMENT` - Unknown template element
- `FUNCTION_CALL_ERROR` - Invalid function call

---

<a name="exceptions"></a>
## Exceptions

### PathNotInBaseDirectoryException

Thrown when an include path is outside the base directory.

**Package:** `com.hindbiswas.jhp.errors.PathNotInBaseDirectoryException`

**Constructor:**
```java
public PathNotInBaseDirectoryException(Path path, Path baseDirectory)
```

### InvalidFileTypeException

Thrown when a file has an invalid type.

**Package:** `com.hindbiswas.jhp.errors.InvalidFileTypeException`

---

## Interfaces

### FunctionLibraryContext

Interface for function libraries.

**Package:** `com.hindbiswas.jhp.engine.FunctionLibraryContext`

**Method:**
```java
Object callFunction(String name, List<Object> args, Deque<Map<String, Object>> scopes)
```

### PathResolver

Interface for resolving template paths.

**Package:** `com.hindbiswas.jhp.engine.PathResolver`

**Method:**
```java
Path resolve(String raw, Path includingFileDir) throws PathNotInBaseDirectoryException
```

### PathToAstParser

Interface for parsing templates to AST.

**Package:** `com.hindbiswas.jhp.engine.PathToAstParser`

**Method:**
```java
TemplateNode parse(Path path) throws Exception
```

### RuntimeIssueResolver

Interface for handling runtime issues.

**Package:** `com.hindbiswas.jhp.engine.RuntimeIssueResolver`

**Method:**
```java
void handle(IssueType type, String message, StringBuilder sb, ThreadLocal<Deque<Path>> includeStack)
```

---

## Built-in Functions Reference

### String Functions

| Function | Description | Example |
|----------|-------------|---------|
| `touppercase(str)` | Convert to uppercase | `{{ touppercase("hello") }}` → `HELLO` |
| `tolowercase(str)` | Convert to lowercase | `{{ tolowercase("HELLO") }}` → `hello` |
| `trim(str)` | Remove whitespace | `{{ trim("  hi  ") }}` → `hi` |

### Utility Functions

| Function | Description | Example |
|----------|-------------|---------|
| `len(value)` | Get length/size | `{{ len("hello") }}` → `5` |
| `now()` | Current timestamp | `{{ now() }}` → `2025-10-05T00:18:03Z` |

---

## Next Steps

- Check out [Examples](examples.md)
- Review [Advanced Usage](advanced-usage.md)
- Explore the source code
