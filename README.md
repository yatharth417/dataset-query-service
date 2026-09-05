Dynamic JSON Dataset Query Service

A Spring Boot RESTful application designed to persist arbitrary JSON records into a relational database and perform dynamic runtime Group-By and Sort-By operations without fixed entity schemas.

🛠 Key Architecture & Design Patterns

Strategy Pattern (strategy/): Isolates the execution logic for dynamic grouping (GroupByStrategy) and sorting (SortByStrategy).

Factory Pattern (QueryStrategyFactory): Evaluates runtime parameters (groupBy, sortBy) and routes execution to the appropriate strategy.

Type-Aware Dynamic Sorting (JsonNodeComparator): Supports numeric, boolean, and text values when sorting JSON records.

Relational Persistence: Uses Spring Data JPA with H2 for local development and PostgreSQL support through a separate profile.

🚀 How to Run

Prerequisites

Java 17 or 21

Maven 3.8+

Git

Postman

Step 1: Run Tests

mvn clean test

Step 2: Start the Application

mvn spring-boot:run

The application runs at:

http://localhost:8080

📡 API Endpoints

1. Insert Record

POST /api/dataset/{datasetName}/record

Example:

POST http://localhost:8080/api/dataset/employee_dataset/record

Request body:

{
  "id": 1,
  "name": "John Doe",
  "age": 30,
  "department": "Engineering"
}

Expected response:

{
  "message": "Record added successfully",
  "dataset": "employee_dataset",
  "recordId": 1
}

2. Group-By Query

GET /api/dataset/{datasetName}/query?groupBy=department

Example:

GET http://localhost:8080/api/dataset/employee_dataset/query?groupBy=department

Response format:

{
  "groupedRecords": {
    "Engineering": [
      {
        "id": 1,
        "name": "John Doe",
        "age": 30,
        "department": "Engineering"
      }
    ]
  }
}

3. Sort-By Query

GET /api/dataset/{datasetName}/query?sortBy=age&order=asc

Example:

GET http://localhost:8080/api/dataset/employee_dataset/query?sortBy=age&order=asc

Response format:

{
  "sortedRecords": [
    {
      "id": 1,
      "name": "John Doe",
      "age": 30,
      "department": "Engineering"
    }
  ]
}

🗄️ Database

H2 is the default profile for local development and testing.

PostgreSQL configuration is available through the postgres profile.