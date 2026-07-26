package com.pulsegrid.processor.service;

import com.pulsegrid.events.telemetry.VehicleTelemetry;
import com.pulsegrid.processor.persistence.VehicleStatusEntity;
import com.pulsegrid.processor.persistence.VehicleStatusRepository;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleStatusProjectionService {

    private final VehicleStatusRepository vehicleStatusRepository;

    public VehicleStatusProjectionService(VehicleStatusRepository vehicleStatusRepository) {
        this.vehicleStatusRepository = vehicleStatusRepository;
    }

    @Transactional
    public void record(VehicleTelemetry telemetry) {
        VehicleStatusEntity status = vehicleStatusRepository.findById(telemetry.getVehicleId())
                .orElseGet(() -> new VehicleStatusEntity(telemetry.getVehicleId()));

        status.setLastSeenAt(Instant.ofEpochMilli(telemetry.getEventTimestamp().toEpochMilli()));
        status.setLatitude(telemetry.getLatitude());
        status.setLongitude(telemetry.getLongitude());
        status.setSpeedKph(telemetry.getSpeedKph());
        status.setFuelOrBatteryLevelPct(telemetry.getFuelOrBatteryLevelPct());
        status.setEngineDiagnosticCode(telemetry.getEngineDiagnosticCode() == null ? null : telemetry.getEngineDiagnosticCode().toString());

        vehicleStatusRepository.save(status);
    }
}
