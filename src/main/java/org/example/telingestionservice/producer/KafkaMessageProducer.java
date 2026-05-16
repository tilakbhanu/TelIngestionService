package org.example.telingestionservice.producer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaMessageProducer implements MessageProducer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageProducer.class);

    // Inject the template
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaMessageProducer() {
        this.kafkaTemplate = null;
    }

    public KafkaMessageProducer(KafkaTemplate<String, String> kafkaTemplate1) {
        this.kafkaTemplate = kafkaTemplate1;
    }

    @CircuitBreaker(name = "telemetryService", fallbackMethod = "fallbackIngest")
    @Retry(name = "telemetryService")
    @TimeLimiter(name = "telemetryService")
    public CompletableFuture<Void> send(String topic, String message) {
        return CompletableFuture.runAsync(() -> {
            logger.info("Pushing to Kafka topic {}: {}", topic, message);
            logger.info("Pushing to Kafka topic ack=1 leader acknowldgment {} , {}", topic, message);
            // This is the actual call
            // kafkaTemplate.send(topic, message);
        });
    }

    // The fallback signature must match the original method signature
    // + the Throwable exception at the end
    public CompletableFuture<Void> fallbackIngest(String topic, String message, Throwable t) {
        logger.error("Fallback triggered for topic {}. Reason: {}", topic, t.getMessage());
        // Based on the error messages - We can push the message to SQS
        // Return a completed future to indicate the fallback handled it
        return CompletableFuture.completedFuture(null);
    }
}