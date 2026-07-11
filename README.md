# Wallex

Wallex is a full-stack personal finance app that lets users upload PDF bank statements, extract transactions, store them in PostgreSQL, and view them through a searchable dashboard.

## Features

- Upload PDF bank statements
- Extract and parse transactions with Apache PDFBox
- Store transaction data in PostgreSQL
- View all transactions in one place
- Search by merchant, category, type, description, date, or amount
- Track total income, expenses, and net savings
- Create, read, update, and delete transactions through REST APIs
- Run the full stack with Docker Compose
- Frontend and backend CI with GitHub Actions

## Tech Stack

**Frontend**
- React
- TypeScript
- Vite
- React Router
- CSS
- Nginx

**Backend**
- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Apache PDFBox
- Maven

**Database and DevOps**
- PostgreSQL
- Docker
- Docker Compose
- GitHub Actions
- JUnit 5
- MockMvc

## Architecture

```text
Browser
   ↓
React + Nginx
   ↓
Spring Boot REST API
   ↓
PostgreSQL
```

PDF import flow:

```text
PDF upload
   ↓
PDFBox text extraction
   ↓
Transaction parser
   ↓
Spring Data JPA
   ↓
PostgreSQL
```

## Running with Docker

### Prerequisites

- Docker Desktop
- Git

### Start the application

```bash
docker compose up --build
```

Open:

```text
http://localhost
```

### Stop the application

```bash
docker compose down
```

Do not use `docker compose down -v` unless you want to delete the PostgreSQL data volume.

## Running Locally

### Start PostgreSQL

```bash
docker compose up -d postgres
```

### Start the backend

```bash
./mvnw spring-boot:run
```

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Backend:

```text
http://localhost:8080
```

### Start the frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend:

```text
http://localhost:5173
```

## Main API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/transactions` | Get all transactions |
| `GET` | `/transactions/{id}` | Get one transaction |
| `POST` | `/transactions` | Create a transaction |
| `PUT` | `/transactions/{id}` | Update a transaction |
| `DELETE` | `/transactions/{id}` | Delete a transaction |
| `POST` | `/imports/pdf` | Upload and import a PDF statement |

## Testing

Backend:

```bash
./mvnw clean verify
```

Frontend:

```bash
cd frontend
npm ci
npm run lint
npm run build
```

## Key Technical Challenges

One of the main challenges was PDF parsing. Some transactions were extracted across multiple lines instead of a single row. The parser was updated to detect transaction start dates, collect related lines into blocks, and then extract the date, merchant, status, and amount.

Another challenge was connecting services across Docker containers. The backend connects to PostgreSQL using the Docker Compose service name instead of `localhost`, and Nginx forwards `/api` requests from the frontend to Spring Boot.


## Author

**Jonathan Thomas**  
Computer Science, Toronto Metropolitan University
