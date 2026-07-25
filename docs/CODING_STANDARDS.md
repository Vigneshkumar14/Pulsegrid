# Coding Standards

## Purpose

Pulsegrid should feel like a real production codebase, not a demo that only works on the happy path. These standards are the default unless a documented exception is approved.

## General Engineering Standards

- Prefer clarity over cleverness.
- Keep methods small and intention-revealing.
- Name classes and packages after domain responsibilities.
- Avoid speculative abstractions.
- Keep mutable shared state to a minimum.
- Fail fast on invalid inputs and configuration.

## Java Standards

- Target Java 21.
- Favor records for immutable DTOs and event payload models where appropriate.
- Prefer constructor injection.
- Avoid field injection.
- Use `Optional` only for return types, not entity fields or DTO properties.
- Keep null-handling explicit.

## Spring Boot Standards

- Use typed `@ConfigurationProperties` instead of scattered string lookups.
- Keep controllers thin.
- Put orchestration in services, not controllers.
- Separate transport models from persistence entities.
- Use Actuator for health and metrics from the beginning.
- Prefer profile-specific config files over large conditional blocks in code.

## Kafka And Streams Standards

- Use Kafka Streams for the processor service.
- Keep topology creation isolated in dedicated configuration classes.
- Give every streams application a stable `application.id`.
- Use explicit SerDes for both keys and values.
- Keep state-store names stable and intentional.
- Route poison or unrecoverable records to a dead-letter topic with enough metadata for diagnosis.
- Do not bury business rules inside serializer or deserializer code.

## Avro Standards

- Store source schemas in `schemas/`.
- Use clear schema names and namespaces.
- Evolve schemas with backward compatibility by default.
- Add fields with sensible defaults when possible.
- Do not reuse the same schema for unrelated event types.
- Keep domain event schema changes reviewed carefully because they affect both services.

## Database Standards

- Use PostgreSQL as the system of record for queryable operational data.
- Manage schema with Flyway.
- Do not edit old migration files after they are shared.
- Prefer explicit column names and indexes.
- Capture timestamps with timezone-aware types.
- Separate append-only telemetry history from mutable current-state projections.

## API Standards

- Version APIs if breaking changes become likely.
- Validate request payloads at the boundary.
- Return meaningful error responses.
- Keep OpenAPI documentation current.
- Do not expose database entities directly from controllers.

## Logging And Observability

- Use structured, contextual logs where practical.
- Include vehicle ID, topic, partition, offset, and correlation-friendly identifiers in processing logs when relevant.
- Log failures with actionability, not noise.
- Expose health and readiness endpoints.

## Testing Standards

- Write unit tests for domain rules and stream transformations.
- Write integration tests for Kafka, Schema Registry, and PostgreSQL interactions.
- Use Testcontainers instead of depending on shared local infrastructure during automated tests.
- Cover negative cases, not only happy paths.

## Review Checklist

- Is the change aligned with the architecture docs?
- Are topic contracts still compatible?
- Are persistence changes backed by migrations?
- Are logs, validation, and error handling adequate?
- Are tests covering the intended behavior?
