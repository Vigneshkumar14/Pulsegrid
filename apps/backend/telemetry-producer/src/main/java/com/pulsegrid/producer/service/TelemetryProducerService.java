package com.pulsegrid.producer.service;

import com.pulsegrid.events.telemetry.VehicleTelemetry;
import com.pulsegrid.producer.api.TelemetryRequest;
import com.pulsegrid.producer.api.TelemetryResponse;
import com.pulsegrid.producer.config.TelemetryTopicsProperties;
import java.time.Instant;
import java.util.UUID;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TelemetryProducerService {

    private final KafkaTemplate<String, VehicleTelemetry> kafkaTemplate;
    private final TelemetryTopicsProperties topics;

    public TelemetryProducerService(
            KafkaTemplate<String, VehicleTelemetry> kafkaTemplate,
            TelemetryTopicsProperties topics
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topics = topics;
    }

    public TelemetryResponse publish(TelemetryRequest request) {
        String eventId = UUID.randomUUID().toString();

        VehicleTelemetry event = VehicleTelemetry.newBuilder()
                .setEventId(eventId)
                .setVehicleId(request.vehicleId())
                .setEventTimestamp(Instant.now().toEpochMilli())
                .setLatitude(request.latitude())
                .setLongitude(request.longitude())
                .setSpeedKph(request.speedKph())
                .setFuelOrBatteryLevelPct(request.fuelOrBatteryLevelPct())
                .setOdometerKm(request.odometerKm())
                .setEngineDiagnosticCode(request.engineDiagnosticCode())
                .build();

        kafkaTemplate.send(topics.telemetry(), request.vehicleId(), event);
        return new TelemetryResponse(eventId, topics.telemetry(), request.vehicleId());
    }
}
