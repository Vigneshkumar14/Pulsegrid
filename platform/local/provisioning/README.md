# Local Provisioning

Pulsegrid uses one-shot local provisioning jobs so a fresh clone can prepare Kafka, Schema Registry, and PostgreSQL dependencies with Docker Compose.

## Provisioning Responsibilities

- `kafka-init`
  Creates the Kafka topics required by the project.
- `schema-registry-init`
  Registers Avro schemas in Schema Registry.
- `db-migrate`
  Applies Flyway SQL migrations to PostgreSQL.

## Why This Shape

This keeps local environment setup deterministic while still following good backend practices:

- topics are infrastructure concerns, so they are provisioned explicitly
- Avro subjects are contract concerns, so they are registered explicitly
- database tables are application concerns, so they are created through Flyway migrations

## Entry Point

Run:

```bash
docker compose up -d
```

That starts the long-running dependencies and also runs the provisioning jobs once.
