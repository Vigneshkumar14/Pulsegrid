package com.pulsegrid.producer.api;

public record TelemetryResponse(String eventId, String topic, String vehicleId) {
}
