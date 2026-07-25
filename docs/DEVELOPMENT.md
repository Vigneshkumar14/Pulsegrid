# Development Guide

## Goal

This guide explains how to start Pulsegrid locally and how to begin backend development without guessing the intended stack, provisioning flow, or repository layout.

## Baseline Versions

- Java 21
- Spring Boot 4.1.0
- PostgreSQL 18.4
- Kafka via Confluent Platform 8.3.0 containers for local development

## Local Prerequisites

- Docker Desktop with Compose support
- JDK 21 installed locally
- Maven 3.9+ or a committed Maven Wrapper once the application modules are scaffolded
- An IDE with strong Spring and Java support

## Start Local Infrastructure

From the repository root:

```bash
docker compose up -d
```

This starts:

- Kafka in KRaft mode
- Schema Registry
- PostgreSQL
- Kafka UI
- `kafka-init` to create topics
- `schema-registry-init` to register Avro subjects
- `db-migrate` to apply Flyway migrations

## Stop Local Infrastructure

```bash
docker compose down
```

Remove the data volumes too:

```bash
docker compose down -v
```

## Default Local Endpoints

- Kafka bootstrap servers: `localhost:9092`
- Schema Registry URL: `http://localhost:8081`
- Kafka UI: `http://localhost:8080`
- PostgreSQL JDBC URL: `jdbc:postgresql://localhost:5432/pulsegrid`
- PostgreSQL user: `pulsegrid`
- PostgreSQL password: `pulsegrid`

## Repository Layout

```text
apps/
|-- backend/
|   |-- telemetry-producer
|   `-- telemetry-processor
`-- frontend/

libs/
`-- java/
    `-- telemetry-contracts

platform/
`-- local/
    `-- provisioning
```

## First Development Milestones

1. Expand the producer simulation model.
2. Expand the Kafka Streams topology in the processor.
3. Persist telemetry history and alerts from the processor flow.
4. Add OpenAPI documentation.
5. Add Testcontainers coverage for Kafka plus PostgreSQL flows.

## Producer Service Expectations

- Simulate vehicles on a schedule.
- Publish Avro messages to `vehicle.telemetry`.
- Use vehicle ID as the record key.
- Expose minimal REST endpoints for simulation control and test event triggering.

## Processor Service Expectations

- Consume `vehicle.telemetry` with Kafka Streams.
- Validate and transform records inside the topology.
- Materialize alerting and latest-state views.
- Persist read models to PostgreSQL.
- Expose REST endpoints for telemetry history, fleet state, and active alerts.

## Configuration Conventions

- Keep local defaults in `application-local.yml`.
- Use environment variables for secrets and container-specific overrides.
- Keep topic names and Schema Registry URLs centralized in typed configuration properties.
- Use separate Spring profiles for `local`, `test`, and `prod`.

## Database Conventions

- Use Flyway from the first persistent table onward.
- Keep DDL in versioned SQL migrations.
- Prefer append-only event history tables plus separate query/read-model tables.
- Index by vehicle ID and event timestamp where query patterns need it.

## Testing Expectations

- Unit test domain logic and stream transformations.
- Use Testcontainers for integration tests.
- Cover the path from produced Avro event to PostgreSQL persistence and alert generation.
- Avoid relying only on manual verification through Kafka UI.

## Daily Workflow

1. Start infrastructure with Docker Compose.
2. Run the producer and processor services locally from the IDE or Maven.
3. Use Kafka UI to inspect topics and message flow.
4. Use PostgreSQL and application APIs to verify read models.
5. Shut down containers when done.

## Backend Commands

Build the full backend:

```bash
mvn test
```

Run the producer service:

```bash
mvn -pl apps/backend/telemetry-producer spring-boot:run
```

Run the processor service:

```bash
mvn -pl apps/backend/telemetry-processor spring-boot:run
```
