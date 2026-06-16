package com.cinemaabyss.events.service;

import com.cinemaabyss.events.model.Event;
import com.cinemaabyss.events.model.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CompletableFuture<EventResponse> sendEvent(String topic, Event event) {
        return kafkaTemplate.send(topic, event.id(), event)
                .thenApply(result -> EventResponse.builder()
                        .status("success")
                        .partition(result.getRecordMetadata().partition())
                        .offset(result.getRecordMetadata().offset())
                        .event(event)
                        .build());
    }
}
