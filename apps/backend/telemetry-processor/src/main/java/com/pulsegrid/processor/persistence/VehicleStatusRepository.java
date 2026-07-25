package com.pulsegrid.processor.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleStatusRepository extends JpaRepository<VehicleStatusEntity, String> {
}
