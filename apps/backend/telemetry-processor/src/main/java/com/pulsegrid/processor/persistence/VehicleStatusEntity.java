package com.pulsegrid.processor.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "vehicle_status")
public class VehicleStatusEntity {

    @Id
    @Column(name = "vehicle_id", nullable = false, length = 100)
    private String vehicleId;

    @Column(name = "last_seen_at", nullable = false)
    private Instant lastSeenAt;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Column(name = "speed_kph", nullable = false)
    private double speedKph;

    @Column(name = "fuel_or_battery_level_pct", nullable = false)
    private double fuelOrBatteryLevelPct;

    @Column(name = "engine_diagnostic_code")
    private String engineDiagnosticCode;

    protected VehicleStatusEntity() {
    }

    public VehicleStatusEntity(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public Instant getLastSeenAt() {
        return lastSeenAt;
    }

    public void setLastSeenAt(Instant lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getSpeedKph() {
        return speedKph;
    }

    public void setSpeedKph(double speedKph) {
        this.speedKph = speedKph;
    }

    public double getFuelOrBatteryLevelPct() {
        return fuelOrBatteryLevelPct;
    }

    public void setFuelOrBatteryLevelPct(double fuelOrBatteryLevelPct) {
        this.fuelOrBatteryLevelPct = fuelOrBatteryLevelPct;
    }

    public String getEngineDiagnosticCode() {
        return engineDiagnosticCode;
    }

    public void setEngineDiagnosticCode(String engineDiagnosticCode) {
        this.engineDiagnosticCode = engineDiagnosticCode;
    }
}
