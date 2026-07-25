package com.pulsegrid.processor.api;

import com.pulsegrid.processor.persistence.VehicleStatusRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fleet")
public class FleetStatusController {

    private final VehicleStatusRepository vehicleStatusRepository;

    public FleetStatusController(VehicleStatusRepository vehicleStatusRepository) {
        this.vehicleStatusRepository = vehicleStatusRepository;
    }

    @GetMapping("/status")
    public List<VehicleStatusResponse> fleetStatus() {
        return vehicleStatusRepository.findAll().stream()
                .map(status -> new VehicleStatusResponse(
                        status.getVehicleId(),
                        status.getLastSeenAt(),
                        status.getLatitude(),
                        status.getLongitude(),
                        status.getSpeedKph(),
                        status.getFuelOrBatteryLevelPct(),
                        status.getEngineDiagnosticCode()
                ))
                .toList();
    }
}
