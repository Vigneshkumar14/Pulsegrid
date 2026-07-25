package com.pulsegrid.producer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pulsegrid.kafka.topics")
public record TelemetryTopicsProperties(String telemetry, String telemetryDlq) {
}
