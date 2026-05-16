package org.example.telingestionservice.controller;

import jakarta.validation.Valid;
import org.example.telingestionservice.model.TelemetryRequest;
import org.example.telingestionservice.producer.KafkaMessageProducer;
import org.example.telingestionservice.producer.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class IngestionController {
    private static final Logger logger = LoggerFactory.getLogger(IngestionController.class);

    private final MessageProducer producer;

    // Dependency Injection
    public IngestionController(MessageProducer producer) {
        logger.info("IngestionController");
        this.producer = producer;
    }

    @PostMapping("/ingest")
    public ResponseEntity<String> ingest(@Valid @RequestBody TelemetryRequest request) {
        if(this.isValidSchema(request)) {
            producer.send("telemetry-topic", request.toString());
            // Return 202 Accepted (Standard for async processing)
            return ResponseEntity.accepted().body("Event Accepted");
        }
        return ResponseEntity.badRequest().body("Invalid Schema");
    }
    @GetMapping("/health")
    public ResponseEntity<String> ingest() {
        return ResponseEntity.ok("Healthy");
    }

    private Boolean isValidSchema(TelemetryRequest request) {
        logger.info("Shouldn't be taken more than 2 - 5 msec to finish this job as thin as possible");
        return true;
    }
}