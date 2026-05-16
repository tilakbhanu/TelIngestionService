package org.example.telingestionservice.producer;

import java.util.concurrent.CompletableFuture;

public interface MessageProducer {
    CompletableFuture<Void> send(String topic, String message);
}