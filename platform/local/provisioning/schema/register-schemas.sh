#!/bin/sh
set -eu

SCHEMA_REGISTRY_URL="${SCHEMA_REGISTRY_URL:-http://schema-registry:8081}"
SCHEMA_FILE="/workspace/schemas/vehicle-telemetry-value.avsc"
SUBJECT="vehicle.telemetry-value"

echo "Waiting for Schema Registry at ${SCHEMA_REGISTRY_URL}..."

until curl -fsS "${SCHEMA_REGISTRY_URL}/subjects" >/dev/null; do
  sleep 2
done

SCHEMA_JSON="$(sed 's/"/\\"/g' "${SCHEMA_FILE}" | tr -d '\n')"
PAYLOAD="{\"schema\":\"${SCHEMA_JSON}\"}"

echo "Registering schema subject ${SUBJECT}..."

curl -fsS -X POST \
  -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  --data "${PAYLOAD}" \
  "${SCHEMA_REGISTRY_URL}/subjects/${SUBJECT}/versions" >/dev/null

echo "Schema registration complete."
