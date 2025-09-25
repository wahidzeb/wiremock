# WireMock Help

This document provides a guide to using, building, and contributing to WireMock.

## Introduction

WireMock is a popular open-source tool for API mock testing. It can help you to create stable test and development environments, isolate yourself from flaky 3rd parties and simulate APIs that don’t exist yet.

## Getting Started

You can run WireMock as a standalone server or as a library in your Java project.

### Standalone Server

The easiest way to get started with the standalone server is to use the official Docker image:

```bash
docker run -it --rm -p 8080:8080 wiremock/wiremock
```

This will start a WireMock server on port 8080.

### Java Library

You can add WireMock to your Java project using Maven or Gradle.

**Maven:**
```xml
<dependency>
    <groupId>org.wiremock</groupId>
    <artifactId>wiremock</artifactId>
    <version>3.0.0-beta-12</version>
    <scope>test</scope>
</dependency>
```

**Gradle:**
```groovy
testImplementation 'org.wiremock:wiremock:3.0.0-beta-12'
```

## Building from Source

To build WireMock from source, you will need a Java Development Kit (JDK) version 11 or higher.

1.  Clone the repository:
    ```bash
    git clone https://github.com/tomakehurst/wiremock.git
    cd wiremock
    ```

2.  Build the project using the Gradle wrapper:
    ```bash
    ./gradlew build
    ```

    This will compile the code, run the tests, and create the JAR files in the `build/libs` directory.

## Running Tests

To run the full test suite, use the following command:

```bash
./gradlew check
```

## Contributing

We welcome contributions to WireMock! Please read our [Contributing Guide](CONTRIBUTING.md) for more information on how to get started.

### Code Style

WireMock uses the [Google Java style guide](https://google.github.io/styleguide/javaguide.html). You can use the `spotlessApply` task to format your code before submitting a pull request:

```bash
./gradlew spotlessApply
```

## Finding Help

If you have a question or need help with WireMock, you can:

*   Join the [WireMock Community Slack](https://slack.wiremock.org/) and ask in the `#help` channel.
*   Ask a question on [Stack Overflow](https://stackoverflow.com/questions/tagged/wiremock) using the `wiremock` tag.
*   Check the [official documentation](https://wiremock.org/docs/).