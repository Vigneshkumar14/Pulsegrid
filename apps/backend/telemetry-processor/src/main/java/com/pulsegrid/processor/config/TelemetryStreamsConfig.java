package com.pulsegrid.processor.config;

import com.pulsegrid.events.telemetry.VehicleTelemetry;
import com.pulsegrid.processor.service.VehicleStatusProjectionService;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;

@Configuration
public class TelemetryStreamsConfig {

    @Bean
    public SpecificAvroSerde<VehicleTelemetry> vehicleTelemetrySerde(KafkaProperties kafkaProperties) {
        SpecificAvroSerde<VehicleTelemetry> serde = new SpecificAvroSerde<>();

        Map<String, Object> config = new HashMap<>(kafkaProperties.buildStreamsProperties());
        serde.configure(config, false);

        return serde;
    }

    @Bean
    public KStream<String, VehicleTelemetry> telemetryStream(
            ObjectProvider<StreamsBuilder> streamsBuilderProvider,
            TelemetryTopicsProperties topics,
            SpecificAvroSerde<VehicleTelemetry> vehicleTelemetrySerde,
            VehicleStatusProjectionService projectionService
    ) {
        StreamsBuilder streamsBuilder = streamsBuilderProvider.getIfAvailable();
        if (streamsBuilder == null) {
            return null;
        }

        KStream<String, VehicleTelemetry> stream = streamsBuilder.stream(
                topics.telemetry(),
                Consumed.with(Serdes.String(), vehicleTelemetrySerde)
        );

        stream.filter((key, value) -> value != null)
                .peek((key, value) -> projectionService.record(value));

        return stream;
    }
}
