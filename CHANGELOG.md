# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2025-10-05

### Added
- Initial release of Java Hypertext Preprocessor (JHP)
- Template engine with intuitive `{{ }}` and `{% %}` syntax
- HTML escaping by default for XSS protection
- Control structures: if/elseif/else, for, foreach, while
- Loop control: break and continue statements
- Template includes with security checks
- Built-in functions: touppercase, tolowercase, trim, len, now
- Custom function registration support
- Context system for variable management
- POJO to Map automatic conversion
- Configurable settings via builder pattern
- Multiple issue handling modes: COMMENT, THROW, DEBUG, IGNORE
- Template caching for improved performance
- Thread-safe concurrent rendering
- Comprehensive unit test suite (88 tests)
- Complete documentation with examples
- Maven Central publishing configuration

### Features
- **Template Syntax**
  - Variable output with `{{ variable }}`
  - Raw HTML output with `{{{ html }}}`
  - Expressions with operators (+, -, *, /, %, ==, !=, <, >, <=, >=, &&, ||, !)
  - Ternary operator support
  - Member access with dot notation
  - Array/list access with square brackets
  - Function calls within expressions

- **Control Structures**
  - If statements with elseif and else
  - For loops with initialization, condition, and update
  - Foreach loops for collections, arrays, and maps
  - While loops
  - Break and continue for loop control
  - Template includes with path resolution

- **Configuration**
  - Base directory setting
  - HTML escaping toggle
  - Maximum include depth limit
  - Issue handling mode selection
  - Builder pattern for easy configuration

- **Security**
  - Automatic HTML escaping
  - Base directory restriction for includes
  - Circular include detection
  - Include depth limiting

- **Performance**
  - Automatic AST caching
  - Thread-safe concurrent rendering
  - Efficient template parsing with ANTLR

### Documentation
- Installation guide
- Getting started tutorial
- Template syntax reference
- Control structures guide
- Functions documentation
- Context and variables guide
- Configuration reference
- Advanced usage patterns
- Complete API reference
- Real-world examples

### Testing
- Context management tests
- Settings configuration tests
- Function library tests (built-in and custom)
- JHP engine integration tests
- AST node structure tests
- Template rendering tests
- Error handling tests

---

## Version History

- **1.0.0** (2025-10-05) - Initial release

[1.0.0]: https://github.com/hind-sagar-biswas/java-hypertext-preprocessor/releases/tag/v1.0.0
[Unreleased]: https://github.com/hind-sagar-biswas/java-hypertext-preprocessor/compare/v1.0.0...HEAD
