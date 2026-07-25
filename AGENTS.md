# AGENTS.md

This file is the working agreement for anyone building Pulsegrid in this repository, whether human or agent.

## Mission

Build a clean, interview-ready, production-minded connected-vehicle telemetry platform with a strong local developer experience.

## Source Of Truth

Read these documents before making structural changes:

1. `docs/ARCHITECTURE.md`
2. `docs/DEVELOPMENT.md`
3. `docs/CODING_STANDARDS.md`
4. `docs/EVENT_CONTRACTS.md`

If implementation choices drift from these docs, update the docs in the same change.

## Core Technical Decisions

- Java 21 is the baseline language version.
- Spring Boot 4.1.0 is the application platform baseline.
- Kafka is the event backbone for telemetry flow.
- Kafka Streams in Spring Boot is the required event consumption model for the processor service.
- Avro is the event contract format.
- Schema Registry is mandatory for topic value schemas.
- PostgreSQL 18.4 is the primary operational datastore.
- Docker Compose is the default local infrastructure entry point.
- Kubernetes is not required for the first development phase.
- Helm charts can exist as deployment design artifacts, but local development stays Docker-first.

## Planned Modules

- `apps/backend/telemetry-producer`
  Produces `vehicle.telemetry` events and exposes control APIs for simulator management.
- `apps/backend/telemetry-processor`
  Consumes telemetry with Kafka Streams, evaluates rules, materializes state, and exposes query APIs.
- `libs/java/telemetry-contracts`
  Generates and shares Avro Java contracts from the repo-level schemas.
- `schemas`
  Stores Avro contracts and related compatibility notes.
- `apps/frontend`
  Reserved for a future React application.
- `platform/local/provisioning`
  Holds one-shot local provisioning jobs for topics, schema registration, and database migrations.
- `platform/helm`
  Holds charts once service bootstrapping is underway.

## Delivery Rules

- Prefer simple, production-sensible designs over clever abstractions.
- Keep service boundaries explicit.
- Model events first, then persistence, then APIs.
- Treat schema evolution as a first-class concern.
- Every externally visible change should have tests or a documented reason why not.
- Flyway should be used for database schema migration once the Spring Boot services are scaffolded.
- Testcontainers should be used for end-to-end integration tests involving Kafka and PostgreSQL.

## Kafka Guidance

- Do not use ad hoc JSON payloads for domain topics that are intended to be Avro-governed.
- Use stable topic names and explicit keys.
- Prefer vehicle ID as the Kafka message key for telemetry.
- Preserve partition ordering per vehicle.
- Favor backward-compatible Avro evolution unless there is a deliberate breaking-change plan.

## Processor Service Guidance

- Use Kafka Streams topologies for validation, enrichment, alert derivation, and state materialization where it fits naturally.
- Keep topology logic testable with focused unit tests.
- Persist query-facing projections to PostgreSQL rather than querying Kafka state stores directly as the only read path.

## Definition Of Done For Early Changes

- Local dependencies start with `docker compose up -d`.
- New contributors can discover how to run the project from `README.md`.
- Event contracts are documented before business logic expands.
- Code follows `docs/CODING_STANDARDS.md`.
