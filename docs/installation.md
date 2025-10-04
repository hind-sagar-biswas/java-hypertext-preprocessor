# Installation

- [Requirements](#requirements)
- [Maven Installation](#maven-installation)
- [Building from Source](#building-from-source)
- [Verifying Installation](#verifying-installation)

<a name="requirements"></a>
## Requirements

JHP has the following requirements:

- Java 25 or higher (Java 24 supported)
- Maven 3.6+ (for building)
- ANTLR 4.13.2 (automatically managed by Maven)

<a name="maven-installation"></a>
## Maven Installation

### Adding to Your Project

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.hindbiswas.jhp</groupId>
    <artifactId>jhp</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Maven Repository

If you're installing from a local repository or custom Maven repository, ensure the repository is configured in your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>local-repo</id>
        <url>file://${project.basedir}/local-repo</url>
    </repository>
</repositories>
```

<a name="building-from-source"></a>
## Building from Source

### Clone the Repository

```bash
git clone https://github.com/hind-sagar-biswas/java-hypertext-preprocessor.git
cd java-hypertext-preprocessor
```

### Build the Project

```bash
mvn clean install
```

This will:
1. Generate ANTLR parser and lexer classes
2. Compile all source files
3. Run the test suite
4. Package the JAR file
5. Install to your local Maven repository

### Build without Tests

If you want to skip tests during build:

```bash
mvn clean install -DskipTests
```

### Generate JAR Only

To create a JAR file without installing to Maven repository:

```bash
mvn clean package
```

The JAR file will be created in the `target/` directory.

<a name="verifying-installation"></a>
## Verifying Installation

### Running Tests

Verify your installation by running the test suite:

```bash
mvn test
```

You should see output indicating all tests passed:

```
Tests run: 88, Failures: 0, Errors: 0, Skipped: 0
```

### Quick Test Program

Create a simple test program to verify JHP is working:

```java
import com.hindbiswas.jhp.*;
import com.hindbiswas.jhp.engine.*;

public class TestJHP {
    public static void main(String[] args) throws Exception {
        Settings settings = Settings.builder().build();
        FunctionLibrary lib = new FunctionLibrary();
        JhpEngine engine = new JhpEngine(settings, lib);
        
        Context ctx = new Context();
        ctx.add("message", "JHP is working!");
        
        // Create a simple template file
        java.nio.file.Files.writeString(
            java.nio.file.Path.of("test.jhp"),
            "{{ message }}"
        );
        
        String result = engine.render("test.jhp", ctx);
        System.out.println(result); // Output: JHP is working!
    }
}
```

## Next Steps

- Read the [Getting Started](getting-started.md) guide
- Learn about [Template Syntax](template-syntax.md)
- Explore [Configuration](configuration.md) options
