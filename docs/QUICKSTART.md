# Quick Start

This guide explains exactly what happens when you run Pulsegrid locally, how provisioning works, how to start both backend services, and how to verify the end-to-end flow.

## 1. Start Local Infrastructure

From the repository root:

```bash
docker compose up -d
```

This starts the long-running infrastructure containers and also runs the one-shot provisioning jobs.

## 2. What Happens During Provisioning

Provisioning is controlled by [docker-compose.yml](/C:/personal_projects/Pulsegrid/docker-compose.yml).

### Startup Order

1. `kafka` starts.
   Docker waits until Kafka passes its healthcheck before treating it as ready.

2. `schema-registry` starts.
   It depends on Kafka being healthy.

3. `postgres` starts.
   Docker waits until PostgreSQL passes its healthcheck before treating it as ready.

4. `kafka-ui` starts.
   It depends on Kafka and Schema Registry.

5. `kafka-init` runs once and exits.
   It depends on Kafka being healthy.
   It executes [create-topics.sh](/C:/personal_projects/Pulsegrid/platform/local/provisioning/kafka/create-topics.sh).

6. `schema-registry-init` runs once and exits.
   It depends on Schema Registry being started.
   It executes [register-schemas.sh](/C:/personal_projects/Pulsegrid/platform/local/provisioning/schema/register-schemas.sh).

7. `db-migrate` runs once and exits.
   It depends on PostgreSQL being healthy.
   It runs Flyway against [V1__init_pulsegrid_tables.sql](/C:/personal_projects/Pulsegrid/apps/backend/telemetry-processor/src/main/resources/db/migration/V1__init_pulsegrid_tables.sql).

### What Gets Provisioned

Kafka topics:

- `vehicle.telemetry`
- `vehicle.telemetry.dlq`

Schema Registry subject:

- `vehicle.telemetry-value`

PostgreSQL tables:

- `vehicle_events`
- `vehicle_status`
- `vehicle_alerts`

## 3. Check That Infrastructure Is Ready

Run:

```bash
docker compose ps
```

Expected result:

- `kafka`, `schema-registry`, `postgres`, and `kafka-ui` should be running
- `kafka-init`, `schema-registry-init`, and `db-migrate` should have exited successfully

Open the local tools:

- Kafka UI: `http://localhost:8080`
- Schema Registry: `http://localhost:8081`
- PostgreSQL: `localhost:5432`

## 4. Run The Backend Services

The Spring Boot services are not yet started by Docker Compose. Run them locally in separate terminals.

Start the producer:

```bash
mvn -pl apps/backend/telemetry-producer spring-boot:run
```

Start the processor:

```bash
mvn -pl apps/backend/telemetry-processor spring-boot:run
```

Default local ports:

- producer: `http://localhost:8082`
- processor: `http://localhost:8083`

## 5. Send A Test Event

Use `curl` to publish one telemetry sample through the producer.

PowerShell:

```powershell
curl -X POST http://localhost:8082/api/v1/telemetry/samples `
  -H "Content-Type: application/json" `
  -d "{\"vehicleId\":\"VH-1001\",\"latitude\":12.9716,\"longitude\":77.5946,\"speedKph\":72.5,\"fuelOrBatteryLevelPct\":48.0,\"odometerKm\":15432.8,\"engineDiagnosticCode\":null}"
```

## 6. Verify The Flow

After sending the request, this is the expected path:

1. the producer accepts the request
2. the producer publishes an Avro event to `vehicle.telemetry`
3. the processor consumes it through Kafka Streams
4. the processor updates the `vehicle_status` projection in PostgreSQL

Query the processor API:

```bash
curl http://localhost:8083/api/v1/fleet/status
```

You should see the vehicle you just published.

## 7. Inspect Kafka And Database State

### Kafka UI

Open `http://localhost:8080` and verify:

- topic `vehicle.telemetry` exists
- topic `vehicle.telemetry.dlq` exists
- messages are arriving in `vehicle.telemetry`
- Schema Registry is visible to the UI

### PostgreSQL

If `psql` is installed locally:

```bash
psql -h localhost -p 5432 -U pulsegrid -d pulsegrid
```

Then inspect the tables:

```sql
select * from vehicle_status;
select * from vehicle_events;
select * from vehicle_alerts;
```

## 8. Useful Logs

Show all container logs:

```bash
docker compose logs -f
```

Show only provisioning logs:

```bash
docker compose logs kafka-init
docker compose logs schema-registry-init
docker compose logs db-migrate
```

## 9. Current Scope Of The Scaffold

Currently implemented:

- local infrastructure startup
- topic creation
- schema registration
- Flyway table creation
- telemetry publishing from the producer
- Kafka Streams consumption in the processor
- `vehicle_status` projection updates
- fleet status query endpoint

Not fully implemented yet:

- full `vehicle_events` persistence
- alert creation in `vehicle_alerts`
- dead-letter handling flow
- full simulation management APIs

## 10. Recovery Notes

If PostgreSQL fails after an image or data-layout change, reset volumes and start again:

```bash
docker compose down -v
docker compose up -d
```
