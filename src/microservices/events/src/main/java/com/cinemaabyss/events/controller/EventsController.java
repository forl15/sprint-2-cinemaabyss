package com.cinemaabyss.events.controller;

import com.cinemaabyss.events.model.*;
import com.cinemaabyss.events.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventsController {

    private final KafkaProducerService producerService;

    @GetMapping("/health")
    public Map<String, Boolean> health() {
        return Map.of("status", true);
    }

    @PostMapping("/movie")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<EventResponse> createMovieEvent(@RequestBody MovieEvent movieEvent) {
        Event event = Event.builder()
                .id(UUID.randomUUID().toString())
                .type("movie")
                .timestamp(Instant.now().toString())
                .payload(movieEvent)
                .build();
        return producerService.sendEvent("movie-events", event);
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<EventResponse> createUserEvent(@RequestBody UserEvent userEvent) {
        Event event = Event.builder()
                .id(UUID.randomUUID().toString())
                .type("user")
                .timestamp(userEvent.timestamp() != null ? userEvent.timestamp() : Instant.now().toString())
                .payload(userEvent)
                .build();
        return producerService.sendEvent("user-events", event);
    }

    @PostMapping("/payment")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<EventResponse> createPaymentEvent(@RequestBody PaymentEvent paymentEvent) {
        Event event = Event.builder()
                .id(UUID.randomUUID().toString())
                .type("payment")
                .timestamp(paymentEvent.timestamp() != null ? paymentEvent.timestamp() : Instant.now().toString())
                .payload(paymentEvent)
                .build();
        return producerService.sendEvent("payment-events", event);
    }
}
