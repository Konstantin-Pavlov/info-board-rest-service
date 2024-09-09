# Info Board REST Service

## Overview

The **Info Board REST Service** is a web application designed to manage user information. It provides a RESTful API for user management, allowing users and messages to be created, retrieved, updated, and deleted. The application is built using Java, Maven, and various modern frameworks and libraries.

## Technologies Used

- **Java 21**: The programming language used for the application.
- **Maven**: Dependency management and build tool.
- **Jersey**: RESTful web services framework.
- **MapStruct**: Code generator for bean mapping.
- **PostgreSQL**: Relational database for data storage.
- **Liquibase**: Database migration tool.
- **JUnit 5**: Testing framework for unit tests.

- ## Project Structure

<pre>
src
├── main
│   ├── java
│   │   └── com
│   │       └── aston
│   │           └── infoBoardRestService
│   │               ├── dao
│   │               ├── dto
│   │               ├── entity
│   │               ├── mapper
│   │               ├── service
│   │                   └── impl
│   │               ├── servlet
│   │               └── util
│   └── resources
│       └── liquibase.properties
│       └── database.properties
└── test
    └── java
        └── com
            └── aston
                └── infoBoardRestService
</pre>


