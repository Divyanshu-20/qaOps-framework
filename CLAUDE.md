# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

# How I Learn

Assume I am learning this framework from scratch.

Do not just write code. Teach me as you build.

For every significant change:

1. Start with the problem.
    - What problem exists?
    - Why do we need this file/class/method?

2. Explain using simple English.
    - Avoid unnecessary jargon.
    - If you use a technical term, explain it immediately.

3. Explain from first principles.
    - Don't assume I know why something exists.
    - Explain the underlying reason before the implementation.

4. Show how it connects to the rest of the project.
    - Which files use it?
    - Which files depend on it?
    - Which files does it depend on?

5. Explain the flow.
    - What happens first?
    - What happens next?
    - What calls what?
    - What data moves where?

6. Think of the project like a tree.
    - Show the roots.
    - Show the branches.
    - Show the leaves.
    - Help me understand where every new piece fits.

7. Every time you create a new file, explain:
    - Why this file exists.
    - Why it cannot simply be merged into another file.
    - Who is responsible for using it.

8. When introducing any design pattern or framework concept:
    - Explain why people invented it.
    - What problem it solves.
    - What would happen if we didn't use it.

9. Never assume something is "obvious."
   If a beginner would ask "Why?", answer it before moving on.

10. Prefer understanding over speed.
    I would rather understand one concept deeply than build ten features quickly.

**Stack:** Java 25, Selenium 4.45.0, TestNG 7.12.0, Maven Surefire 3.5.6

## Project Overview

This is a Selenium + TestNG test automation framework targeting the OrangeHRM demo application (`https://opensource-demo.orangehrmlive.com`). It uses Maven for dependency management and build orchestration.

## Commands

```bash
# Run all tests
mvn test

# Run tests using a specific TestNG suite
mvn test -DsuiteXmlFile=src/test/resources/testng.xml

# Compile without running tests
mvn compile test-compile

# Run a single test class
mvn test -Dtest=LoginTest

# Clean and rebuild
mvn clean test
```

## Architecture

### Package Structure

All test code lives under `src/test/java/com/automation/orangehrm/`:

- **`base/BaseClass.java`** — Parent class for all test classes. Handles `config.properties` loading and `WebDriver` initialization. Browser selection (`chrome`, `edge`) is driven by the `browser` property. Test classes must call `setup()` (typically via a TestNG `@BeforeMethod`) before using `driver`.

- **`actiondriver/ActionDriver.java`** — Utility class intended to wrap common Selenium interactions (click, type, wait, etc.). Currently a stub; add reusable action methods here rather than duplicating Selenium calls across page objects or tests.

### Configuration

`src/test/resources/config.properties` centralizes all environment settings:

| Key | Purpose |
|-----|---------|
| `url` | AUT base URL |
| `browser` | Browser to launch (`chrome` or `edge`) |
| `username` / `password` | Default login credentials |
| `implicitWait` | Global implicit wait in seconds |

`BaseClass.setup()` reads this file via `FileInputStream` with a relative path (`src/test/resources/config.properties`), so tests must be launched from the project root.

### Intended Layering

As the framework grows, follow this layered pattern:
1. **Base layer** (`base/`) — driver lifecycle, config, common setup/teardown
2. **Action driver layer** (`actiondriver/`) — reusable Selenium wrappers
3. **Page Object layer** (add `pages/`) — one class per page, using `ActionDriver` methods
4. **Test layer** (add `tests/`) — TestNG test classes extending `BaseClass`

### TestNG Suite

`src/test/resources/testng.xml` is the suite file (currently empty skeleton). Wire test classes into it and reference it via `-DsuiteXmlFile` when running targeted suites.

## Skill routing

When the user's request matches an available skill, invoke it via the Skill tool. When in doubt, invoke the skill.

Key routing rules:
- Product ideas/brainstorming → invoke /office-hours
- Strategy/scope → invoke /plan-ceo-review
- Architecture → invoke /plan-eng-review
- Design system/plan review → invoke /design-consultation or /plan-design-review
- Full review pipeline → invoke /autoplan
- Bugs/errors → invoke /investigate
- QA/testing site behavior → invoke /qa or /qa-only
- Code review/diff check → invoke /review
- Visual polish → invoke /design-review
- Ship/deploy/PR → invoke /ship or /land-and-deploy
- Save progress → invoke /context-save
- Resume context → invoke /context-restore
- Author a backlog-ready spec/issue → invoke /spec
