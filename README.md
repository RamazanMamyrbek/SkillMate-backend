# SkillMate Backend

## Project Overview

SkillMate is a skill exchange platform that allows users to connect and share their knowledge without financial costs. The platform enables people to find partners for skill exchange based on mutual interests. For example, a user can offer guitar lessons in exchange for English tutoring. Key features include user profiles, skill matching, in-app communication, ratings and reviews, and a resource-sharing library.

## About the Project

SkillMate Backend is the server-side part of the SkillMate project, developed using Java Spring Boot. The project includes user authentication, profile image uploading, and other features.

## Technology Stack

- **Java 21**
- **Spring Boot**
- **Spring Security**
- **MinIO** (for file storage)
- **PostgreSQL** (as the main database)
- **Docker, Docker Compose**

## Running the Project

### 1. Start the Infrastructure (Database, File Server, and Other Services)

Navigate to the `infra` folder and start `docker-compose`:

```sh
cd infra
docker-compose up -d
```

### 2. Start the Backend

Build and run the application:

```sh
./mvnw clean install
java -jar target/*.jar
```

## Environment Variables

Before starting, make sure the required environment variables are set:

```sh
export DATABASE_URL=jdbc:postgresql://localhost:5432/skillmate
export DATABASE_USER=postgres
export DATABASE_PASSWORD=yourpassword
export MINIO_URL=http://localhost:9000
export MINIO_ACCESS_KEY=minioadmin
export MINIO_SECRET_KEY=minioadmin
```

## API Documentation

API documentation is available at: `http://localhost:8080/swagger-ui.html`

## Contacts

- Author: **Ramazan Mamyrbek**
- GitHub: [RamazanMamyrbek](https://github.com/RamazanMamyrbek)