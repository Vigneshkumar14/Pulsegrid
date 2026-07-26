# Pulsegrid

Pulsegrid is a backend-first connected-vehicle telemetry platform built with Java 21, Spring Boot, Kafka, Avro, and PostgreSQL.

The first development milestone is a local-first event pipeline:

- a telemetry producer service that emits vehicle events
- a telemetry processor service that consumes events with Kafka Streams
- Avro schemas stored in Schema Registry
- PostgreSQL for queryable operational data
- Docker Compose for one-command local infrastructure startup

## Quick Start

Start the local dependencies and provisioning jobs:

```bash
docker compose up -d
```

Open the local tools:

- Kafka broker: `localhost:9092`
- Schema Registry: `http://localhost:8081`
- Kafka UI: `http://localhost:8080`
- PostgreSQL: `localhost:5432`

One-shot provisioning jobs also run during startup to:

- create Kafka topics
- register Avro schemas in Schema Registry
- apply Flyway database migrations

Stop everything:

```bash
docker compose down
```

Remove volumes too:

```bash
docker compose down -v
```

If PostgreSQL fails after an image upgrade, reset the local Postgres volume and start again:

```bash
docker compose down -v
docker compose up -d
```

## Planned Stack

- Java 21
- Spring Boot 4.1.0
- Spring for Apache Kafka with Kafka Streams
- Apache Avro with Schema Registry
- PostgreSQL 18.4
- Docker Compose for local infrastructure
- Helm charts prepared later for deployment modeling

## Monorepo Layout

```text
.
|-- apps/
|   |-- backend/
|   |   |-- telemetry-producer/
|   |   `-- telemetry-processor/
|   `-- frontend/
|-- libs/
|   `-- java/
|       `-- telemetry-contracts/
|-- platform/
|   `-- local/
|       `-- provisioning/
|-- docs/
|-- schemas/
`-- pom.xml
```

## Backend Modules

- `libs/java/telemetry-contracts`
  Shared Avro-generated Java classes from the repo-level schemas.
- `apps/backend/telemetry-producer`
  Spring Boot service that publishes Avro telemetry events.
- `apps/backend/telemetry-processor`
  Spring Boot service that consumes telemetry with Kafka Streams and projects fleet status into PostgreSQL.

## Build And Run

Build the backend:

```bash
mvn test
```

Run the producer:

```bash
mvn -pl apps/backend/telemetry-producer spring-boot:run
```

Run the processor:

```bash
mvn -pl apps/backend/telemetry-processor spring-boot:run
```

## Repo Docs

- [Architecture](docs/ARCHITECTURE.md)
- [Development Guide](docs/DEVELOPMENT.md)
- [Coding Standards](docs/CODING_STANDARDS.md)
- [Event Contracts](docs/EVENT_CONTRACTS.md)
- [Agent Working Guide](AGENTS.md)

## Notes

- The Docker Compose stack provisions local topics, Avro subjects, and database tables automatically.
- The processor service uses Kafka Streams, not a plain `@KafkaListener`, for event consumption and stateful stream processing.
- Version choices in this repo were aligned with the current stable releases verified on July 25, 2026.
