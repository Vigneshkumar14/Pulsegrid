# Pulsegrid

**A connected-vehicle telemetry pipeline built with Spring Boot, Kafka, Avro, PostgreSQL, and Helm.**

Pulsegrid simulates a fleet of connected vehicles emitting telemetry such as GPS location, speed, fuel or battery level, and engine diagnostics. The system streams that data through Kafka, processes it with Kafka Streams, applies real-time alerting logic, persists queryable views in PostgreSQL, and exposes the results through REST APIs.

---

## Why This Project

This project is meant to demonstrate hands-on Spring Boot development, event-driven architecture, streaming fundamentals, and deployment-minded design in a domain that feels realistic and easy to talk about in interviews.

---

## Architecture Overview

```text
Vehicle Simulator
    |
    v
Telemetry Producer Service
    - Spring Boot
    - scheduled simulation
    - REST API
    - KafkaTemplate
    |
    v
Kafka
    - topic: vehicle.telemetry
    - topic: vehicle.telemetry.dlq
    - Avro schemas in Schema Registry
    |
    v
Telemetry Processor Service
    - Spring Boot
    - Kafka Streams
    - validation
    - alert rules
    - PostgreSQL persistence
    - REST API
    |
    v
PostgreSQL
    - vehicle_events
    - vehicle_status
    - vehicle_alerts
```

---

## Components

### 1. Vehicle Telemetry Producer Service

- Built with Spring Boot
- Simulates a configurable fleet of virtual vehicles on a schedule
- Publishes Avro telemetry events to Kafka with Spring Kafka `KafkaTemplate`
- Exposes REST endpoints to:
  - register or configure simulated vehicles
  - manually trigger telemetry events
  - inject fault-code scenarios for testing
  - list active simulated vehicles

### 2. Vehicle Telemetry Processor Service

- Built with Spring Boot
- Consumes telemetry using Kafka Streams
- Validates incoming records
- Routes malformed or unrecoverable records to `vehicle.telemetry.dlq`
- Persists valid events and read models to PostgreSQL
- Maintains the latest known status per vehicle
- Evaluates alert rules such as:
  - low fuel or battery
  - overspeed
  - diagnostic fault present
  - stale or inactive vehicle
- Exposes REST endpoints to:
  - query telemetry history by vehicle
  - query fleet status
  - query active alerts

### 3. Event Backbone

- Kafka is the central event backbone
- Main topic: `vehicle.telemetry`
- Dead-letter topic: `vehicle.telemetry.dlq`
- Avro is the event serialization format
- Schema Registry governs event contract versions and compatibility

### 4. Data Store

- PostgreSQL stores the queryable operational data
- `vehicle_events` keeps the append-only event history
- `vehicle_status` keeps the latest known vehicle state
- `vehicle_alerts` keeps generated alerts and their lifecycle state

### 5. Deployment Artifacts

- Helm charts can be added for deployment modeling
- Kubernetes deployment is not required for the first development phase
- Initial development is local-first and Docker Compose-first

### 6. Local Environment

- `docker-compose.yml` starts Kafka, Schema Registry, PostgreSQL, Kafka UI, and topic initialization
- Contributors should be able to start local dependencies with one command

---

## Stretch Additions

- Testcontainers end-to-end integration tests
- Spring Boot Actuator health and metrics endpoints
- fleet dashboard summary endpoint
- geofence-based alert rule

---

## What This Demonstrates

| Requirement | How Pulsegrid shows it |
|---|---|
| Spring Boot experience | Two services built end-to-end in Spring Boot |
| Kafka and event-driven systems | Producer plus Kafka Streams processor with dead-letter handling |
| Avro and event contracts | Schema-managed topic payloads through Schema Registry |
| PostgreSQL and SQL | Queryable operational read models and event history |
| REST API design | Documented service endpoints with OpenAPI support |
| Deployment thinking | Docker Compose locally and Helm artifacts for later deployment modeling |
| Operational thinking | Validation, DLQ handling, stale-vehicle detection, alerting |
