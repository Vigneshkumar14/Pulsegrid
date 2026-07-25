package com.pulsegrid.producer.api;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record TelemetryRequest(
        @NotBlank String vehicleId,
        @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0") double latitude,
        @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0") double longitude,
        @DecimalMin("0.0") double speedKph,
        @DecimalMin("0.0") @DecimalMax("100.0") double fuelOrBatteryLevelPct,
        @DecimalMin("0.0") double odometerKm,
        String engineDiagnosticCode
) {
}
