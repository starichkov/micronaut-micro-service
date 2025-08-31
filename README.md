[![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/starichkov/micronaut-micro-service/maven.yml?style=for-the-badge)](https://github.com/starichkov/micronaut-micro-service/actions/workflows/maven.yml)
[![codecov](https://img.shields.io/codecov/c/github/starichkov/micronaut-micro-service?style=for-the-badge)](https://app.codecov.io/github/starichkov/micronaut-micro-service)
[![GitHub license](https://img.shields.io/github/license/starichkov/micronaut-micro-service?style=for-the-badge)](https://github.com/starichkov/micronaut-micro-service/blob/main/LICENSE.md)

Micronaut microservice
=
This project is a Micronaut framework based, 'ready-to-play' micro-service with a simple UI for managing notes and tags.

## Features

- RESTful API for managing notes and tags
- Simple and responsive UI for creating, editing, and deleting notes and tags
- Ability to associate tags with notes for better organization
- Full-text search in notes (coming soon)

## Technical information

| Name       | Version |
|------------|---------|
| Java       | 21      |
| Maven      | 3.8.1+  |
| Micronaut  | 4.9.3   |
| PostgreSQL | 17.6    |

## Prerequisites

### Database Setup

The application requires a PostgreSQL database. You need to:

1. Install PostgreSQL 17 or later
2. Create a database named `micro_notes` (or use a custom name and configure it in environment variables)
   ```postgresql
   CREATE DATABASE micro_notes;
   CREATE USER micro_user WITH PASSWORD '<<password_here>>';
   GRANT ALL PRIVILEGES ON DATABASE micro_notes TO micro_user;
   ```
3. Set the following environment variables:
   - `DATABASE_HOST` - PostgreSQL host (default: localhost)
   - `DATABASE_PORT` - PostgreSQL port (default: 5432)
   - `DATABASE_NAME` - Database name (default: micro_notes)
   - `DATABASE_USER` - Database username
   - `DATABASE_PASS` - Database password

The application uses Flyway for database migrations, which will automatically create the necessary tables when the application starts.

## Running the Application

### Standard Run

```shell
mvn mn:run
```

The application will start on port 8080 by default. You can access the UI by navigating to:

```
http://localhost:8080
```

### Docker Run

#### Using Micronaut Maven Plugin

```shell
# Build the Docker image
mvn clean package -Dpackaging=docker

# Run the Docker container
docker run -p 8080:8080 micronaut-micro-service
```

#### Using Docker Compose

This project includes a Docker Compose setup that runs both the application and a PostgreSQL database.

The project uses a `.env` file to store environment variables for Docker Compose, including database credentials. This approach prevents sensitive information from being hardcoded in the docker-compose.yml file.

```shell
# Build and start the containers
docker-compose up -d

# Stop the containers
docker-compose down

# Stop the containers and remove volumes
docker-compose down -v
```

The Docker Compose setup includes:
- The application running on port 8080
- PostgreSQL 17.6 with Alpine 3.22 running on port 5432
- Health checks for both services
- Volume for PostgreSQL data persistence
- Environment variables loaded from `.env` file

##### Environment Variables

The `.env` file contains the following variables:
- `DATABASE_HOST`: PostgreSQL host (set to "postgres" for Docker Compose)
- `DATABASE_PORT`: PostgreSQL port
- `DATABASE_NAME`: Database name
- `DATABASE_USER`: Database username
- `DATABASE_PASS`: Database password
- `POSTGRES_DB`: PostgreSQL database name
- `POSTGRES_USER`: PostgreSQL username
- `POSTGRES_PASSWORD`: PostgreSQL password

You can modify these variables in the `.env` file to customize your setup.

## Using the Notes and Tags UI

The UI provides a simple interface for managing your notes and tags. The interface is divided into two main sections:

- **Notes Section**: For creating, viewing, editing, and deleting notes
- **Tags Section**: For creating, viewing, editing, and deleting tags

There's also a tag management panel that appears when you click "Manage Tags" on a note, allowing you to associate tags with notes.

### Working with Notes

1. **View Notes**: All your notes are displayed in the Notes section.
2. **Create a Note**: 
   - Fill in the title and content in the "Add/Edit Note" form
   - Click "Save"
3. **Edit a Note**:
   - Click the "Edit" button on a note
   - Update the title and/or content
   - Click "Save"
4. **Delete a Note**:
   - Click the "Delete" button on a note
   - Confirm the deletion

### Working with Tags

1. **View Tags**: All your tags are displayed in the Tags section.
2. **Create a Tag**:
   - Enter a label in the "Add/Edit Tag" form
   - Click "Save"
3. **Edit a Tag**:
   - Click the "Edit" button on a tag
   - Update the label
   - Click "Save"
4. **Delete a Tag**:
   - Click the "Delete" button on a tag
   - Confirm the deletion

### Managing Tags for Notes

1. Click the "Manage Tags" button on a note
2. In the tag management panel:
   - Click on an available tag to add it to the note
   - Click on a selected tag to remove it from the note
3. Click "Close" when finished

## API Endpoints

### Notes API

- `GET /notes` - Get all notes
- `GET /notes/{id}` - Get a specific note
- `POST /notes` - Create a new note
- `PATCH /notes/{id}` - Update a note
- `DELETE /notes/{id}` - Delete a note
- `POST /notes/{noteId}/tags/{tagId}` - Add a tag to a note
- `DELETE /notes/{noteId}/tags/{tagId}` - Remove a tag from a note

### Tags API

- `GET /tags` - Get all tags
- `GET /tags/{id}` - Get a specific tag
- `POST /tags` - Create a new tag
- `PATCH /tags/{id}` - Update a tag
- `DELETE /tags/{id}` - Delete a tag

### Health API

- `GET /health` - Get application health status
  - Returns a JSON object with the following structure:
    ```json
    {
      "status": "UP",
      "timestamp": 1234567890123,
      "database": {
        "status": "UP"
      }
    }
    ```
  - If the database is down, the response will include an error message and the overall status will be "DOWN"

## GraalVM build

### Generate a native executable using Maven

```shell
mvn clean package -Pgraalvm -Dpackaging=native-image
```

### Generate a native executable inside Docker

```shell
mvn clean package -Dpackaging=docker-native
```

---

## Micronaut

- [Releases](https://github.com/micronaut-projects/micronaut-core/releases)
- [User Guide](https://docs.micronaut.io/latest/guide/index.html)
- [API Reference](https://docs.micronaut.io/latest/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/latest/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)

---

## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)

---

## 🧾 About TemplateTasks

TemplateTasks is a personal software development initiative by Vadim Starichkov, focused on sharing open-source libraries, services, and technical demos.

It operates independently and outside the scope of any employment.

All code is released under permissive open-source licenses. The legal structure may evolve as the project grows.

## 📜 License & Attribution

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE.md) file for details.

### Using This Project?

If you use this code in your own projects, attribution is required under the MIT License:

```
Based on micronaut-micro-service by Vadim Starichkov, TemplateTasks

https://github.com/starichkov/micronaut-micro-service
```

**Copyright © 2025 Vadim Starichkov, TemplateTasks**
