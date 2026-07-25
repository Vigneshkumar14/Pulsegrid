package com.pulsegrid.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TelemetryProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelemetryProducerApplication.class, args);
    }
}
