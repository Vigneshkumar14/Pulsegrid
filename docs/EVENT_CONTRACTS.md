# Event Contracts

## Purpose

Pulsegrid uses Avro plus Schema Registry so event contracts stay explicit, versioned, and safe to evolve.

## Initial Topics

- `vehicle.telemetry`
  Main telemetry event stream.
- `vehicle.telemetry.dlq`
  Dead-letter stream for records that fail validation or processing.

## Keying Strategy

- Kafka message key: `vehicleId`
- Reason: all events for one vehicle should remain ordered within a partition

## Schema Registry Conventions

- Use Schema Registry for Avro value schemas.
- Keep value subjects compatible with the default topic naming convention unless there is a strong reason to override it.
- Expected subject for the main value schema: `vehicle.telemetry-value`

## Initial Schema Files

- [vehicle-telemetry-value.avsc](../schemas/vehicle-telemetry-value.avsc)

## Avro Naming Conventions

- Namespace: `com.pulsegrid.events.telemetry`
- Schema names should be singular and domain-specific.
- Prefer explicit field docs for non-obvious fields.

## Evolution Rules

- Default to backward compatibility.
- New optional fields should include defaults.
- Do not rename or remove fields casually.
- If a truly breaking change is required, create a new topic and schema lineage instead of quietly mutating the old one.

## Suggested Event Semantics

- One message represents one telemetry observation from one vehicle at one point in time.
- Event time should be carried in the payload and not inferred from Kafka metadata alone.
- Keep the domain payload focused; enrichments that belong to processing should happen downstream.

## Processor Expectations

- Validate required fields before business processing.
- Route unrecoverable records to `vehicle.telemetry.dlq`.
- Keep enough metadata in DLQ headers or payload for troubleshooting.
