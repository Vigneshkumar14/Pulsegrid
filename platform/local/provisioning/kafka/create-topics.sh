#!/bin/sh
set -eu

echo "Creating Pulsegrid Kafka topics..."

kafka-topics --bootstrap-server kafka:29092 --create --if-not-exists --topic vehicle.telemetry --partitions 6 --replication-factor 1
kafka-topics --bootstrap-server kafka:29092 --create --if-not-exists --topic vehicle.telemetry.dlq --partitions 3 --replication-factor 1

echo "Kafka topic provisioning complete."
