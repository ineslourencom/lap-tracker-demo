# LapTracker Service

## Project Overview
- Language: Java 21
- Framework: Spring Boot 4.0.0
- Build Tool: Maven
## Key Configuration
- dependencies including:
  - spring-boot-starter-web
  - spring-boot-starter-data-jpa (Persistence)
  - postgresql (Database driver)
  - flyway-core (Database migration)
  - lombok (Boilerplate reduction)
  - testcontainers (Integration testing with PostgreSQL)

## Main topics:
- 5 drivers
- fixed number of laps (driving from the start/finish line to the start/finish line).
- timing system receives info each time driver passes start/finish line: retrieved info kart number + instant
- race is finished when drivers complete all laps
- winner: driver with fastest lap

## Features:
- Start race
- Finish race
- get winning results of the most recent race: 
  - kart number
  - Fastest Lap
    - lap duration (finish - start time)
    - lap number
    - start time


## Data Diagram/ Objects domain:
- Race
  - list<kart>
  - number_of_laps
- kart
  - id
  - kart_number
  - list<laps>
- Lap
  - start_time
  - finish_time
  - number_lap? > define this


## Technical decisions:
- Outbox pattern to receive Lap information for data consistency and reliability
- HTTP calls for starting and finish a race
- Given fast data input and for this example, decided to generate the ids by sequence
- Command Query Responsibility Segregation (CQRS): segregate write from upsert operations since we want to achieve better results when fetching results and when upserting new
- 