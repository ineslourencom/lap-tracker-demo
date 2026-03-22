# LapTracker Service

A Spring Boot application for tracking kart race laps, recording passage times, and calculating race results.

## Tech Stack
*   **Java 21**
*   **Spring Boot 4.0.4+** (Web, Data JPA, AMQP)
*   **PostgreSQL** (Database)
*   **RabbitMQ** (Message Queue for async processing)
*   **Docker & Docker Compose** to support DB and RabbitMQ

## Prerequisites
*   Docker & Docker Compose
*   Java 21 (for local development)
*   Maven

## How to Run

### Option 1: Docker (Recommended)
Run the entire stack (App, DB, RabbitMQ) with a single command:

```bash
docker-compose up --build
```

The application will be available at `http://localhost:8080`.

### Option 2: Local Development
1.  Start dependencies (PostgreSQL & RabbitMQ):
    ```bash
    docker-compose up db rabbitmq -d
    ```
2.  Run the application:
    ```bash
    ./mvnw spring-boot:run
    ```

## Testing
Run the full test suite (Unit + Integration tests using Testcontainers):

```bash
./mvnw verify
```

## API Overview

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/v1/races/start` | Start a new race |
| `POST` | `/api/v1/passages` | Record a kart passage (Async processing via RabbitMQ) |
| `POST` | `/api/v1/races/{raceId}/finish` | Finish a race |
| `GET` | `/api/v1/races/{raceId}/result` | Get race results (Winner, Fastest Lap) |

## Key Assumptions & Architecture
*   **Race Logic:** Fixed number of laps; winner is determined by completing all laps first. Fastest lap is also calculated.
*   **Async Processing:** Passage recording is decoupled using RabbitMQ to ensure high throughput and reliability.
*   **Data Model:** 
    *   `Race`: Contains list of Karts.
    *   `Kart`: Identified by kart number.
    *   `Passage`: Time instant when a kart passes the sensor. This will allow us to calculate the laps


## Disclaimer
This project uses AI for code improvements, testing, and documentation improvements. Core architecture and data schema design are original.
