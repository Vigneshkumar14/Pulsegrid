package com.pulsegrid.processor.api;

import java.time.Instant;

public record VehicleStatusResponse(
        String vehicleId,
        Instant lastSeenAt,
        double latitude,
        double longitude,
        double speedKph,
        double fuelOrBatteryLevelPct,
        String engineDiagnosticCode
) {
}
