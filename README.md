# Dynamic JSON Dataset Query Service

A Spring Boot RESTful application designed to persist arbitrary JSON records into a relational database and perform dynamic runtime **Group-By** and **Sort-By** operations.

---

## 🛠️ Key Architecture & Design Patterns

- **Strategy Pattern** — `GroupByStrategy` and `SortByStrategy` isolate query execution logic.
- **Factory Pattern** — `QueryStrategyFactory` selects the appropriate strategy based on query parameters.
- **Type-Aware Sorting** — `JsonNodeComparator` supports numeric, boolean, and text values when sorting JSON records.
- **Relational Persistence** — Uses Spring Data JPA with H2 for local development and PostgreSQL support through a separate profile.

---

## 🚀 How to Run

### Prerequisites

- Java 17 or 21
- Maven 3.8+
- Git
- Postman

### Run Tests

```bash
mvn clean test
```

### Start the Application

```bash
mvn spring-boot:run
```

The application runs at:

```text
http://localhost:8080
```

---

## 📡 API Endpoints

### 1. Insert Record

**Request**

```http
POST /api/dataset/{datasetName}/record
```

**Example**

```http
POST http://localhost:8080/api/dataset/employee_dataset/record
```

**Request Body**

```json
{
  "id": 1,
  "name": "John Doe",
  "age": 30,
  "department": "Engineering"
}
```

**Response**

```json
{
  "message": "Record added successfully",
  "dataset": "employee_dataset",
  "recordId": 1
}
```

### 2. Group-By Query

**Request**

```http
GET /api/dataset/{datasetName}/query?groupBy=department
```

**Example**

```http
GET http://localhost:8080/api/dataset/employee_dataset/query?groupBy=department
```

**Response**

```json
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
```

### 3. Sort-By Query

**Request**

```http
GET /api/dataset/{datasetName}/query?sortBy=age&order=asc
```

**Example**

```http
GET http://localhost:8080/api/dataset/employee_dataset/query?sortBy=age&order=asc
```

**Response**

```json
{
  "sortedRecords": [
    {
      "id": 2,
      "name": "Jane Smith",
      "age": 25,
      "department": "Engineering"
    },
    {
      "id": 3,
      "name": "Alice Brown",
      "age": 28,
      "department": "Marketing"
    },
    {
      "id": 1,
      "name": "John Doe",
      "age": 30,
      "department": "Engineering"
    }
  ]
}
```

---

## 🗄️ Database

- **H2** — default profile for local development and testing.
- **PostgreSQL** — available through the `postgres` profile.

---

## 🧪 Testing

The project includes tests for:

- REST controller behavior
- Service-layer behavior
- JSON comparison and sorting

Run all tests with:

```bash
mvn clean test
```

---

## 📁 Project Structure

```text
src/
├── main/
│   ├── java/com/assignment/dataset/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── exception/
│   │   ├── repository/
│   │   ├── service/
│   │   ├── strategy/
│   │   └── util/
│   └── resources/
│
└── test/
    └── java/com/assignment/dataset/
        ├── controller/
        ├── service/
        └── strategy/
```
