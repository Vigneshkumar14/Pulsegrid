package com.pulsegrid.producer.api;

import com.pulsegrid.producer.service.TelemetryProducerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/telemetry")
public class TelemetryProducerController {

    private final TelemetryProducerService telemetryProducerService;

    public TelemetryProducerController(TelemetryProducerService telemetryProducerService) {
        this.telemetryProducerService = telemetryProducerService;
    }

    @PostMapping("/samples")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TelemetryResponse publish(@Valid @RequestBody TelemetryRequest request) {
        return telemetryProducerService.publish(request);
    }
}
