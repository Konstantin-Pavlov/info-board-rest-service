# Info Board REST Service

<details>
  <summary><strong>how to launch:</strong></summary>

- **install tomcat 8.5.97** 
- **go to configuration:**
  <br> ![image](https://github.com/user-attachments/assets/142fb365-f56a-40fe-a6d7-233a99170d32)
  <br>
- **choose tomcat server -> local**
  <br> ![image](https://github.com/user-attachments/assets/b2a73286-ecc7-4518-9cf6-98cd753a66b9)
  <br>
- **add artifact**
  <br> ![image](https://github.com/user-attachments/assets/0cfa8181-9092-4dc8-8002-47f748611e3f)
  <br>
- **choose exploded artifact**
  <br> ![image](https://github.com/user-attachments/assets/0c23847a-b2fd-4d6b-a342-8ce65d86190c)
  <br>
- **from application context only '/' remains**
  <br> ![image](https://github.com/user-attachments/assets/98729444-9d9d-47e6-ab94-3ec38fa7a008)
  <br>
- **start docker and launch compose.yaml**
  <br> ![image](https://github.com/user-attachments/assets/496a0f6c-3e8f-4d82-b1aa-400ef51c9cc3)
  <br>
  </details>

### JSON for Postman

- Add **[json_for_postman.json](json_for_postman.json)** to Postman.
    - **Steps to Import JSON into Postman:**
        - Open Postman.
        - Click on the "Import" button located in the top-left corner.
        - In the Import window, select the "Upload Files" tab.
        - Drag and drop the `json_for_postman.json` file into the window or click "Choose Files" to browse and select
          the file.
        - Click the "Import" button to add the collection to Postman.
        - The imported collection will appear in the Collections sidebar, and you can now use the predefined endpoints
          for testing.
          <br>
          <br>

### ISSUE: CONTROLLERS TESTS IS NOT WORKING

- probable cause: perhaps because the service object is initialized in the controller itself.
- solution: Move the initialization somewhere else?

## Overview

The **Info Board REST Service** is a web application designed to manage user information. It provides a RESTful API for
user management, allowing users and messages to be created, retrieved, updated, and deleted. The application is built
using Java, Maven, and various modern frameworks and libraries.

## Technologies Used

- **Java 21**: The programming language used for the application.
- **Maven**: Dependency management and build tool.
- **Jersey**: RESTful web services framework.
- **MapStruct**: Code generator for bean mapping.
- **PostgreSQL**: Relational database for data storage.
- **Liquibase**: Database migration tool.
- **Docker**: Platform for developing, shipping, and running applications inside lightweight, portable containers.
- **JUnit 5**: Testing framework for unit tests.

<details>
  <summary><strong>Endpoints</strong></summary>

### User Endpoints

- **GET /users**: Retrieve a list of all users.
- **GET /users/{id}**: Retrieve a specific user by their ID.
- **GET /users/{email}**: Retrieve a specific user by their email.
- **POST /users**: Create a new user.
    - **Request Body**: JSON object containing user details.
- **DELETE /users/{email}**: Delete a user by their email.

### Message Endpoints

- **GET /messages**: Retrieve a list of all messages.
- **GET /messages/{id}**: Retrieve a specific message by its ID.
- **POST /messages**: Create a new message.
    - **Request Body**: JSON object containing message details.
- **DELETE /messages/{id}**: Delete a message by its ID.

</details>

## Project Structure

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
│   │                   └── api
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


