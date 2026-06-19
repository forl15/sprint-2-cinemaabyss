package com.cinemaabyss.events.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "movie-events", groupId = "events-group")
    public void consumeMovieEvent(Object event) {
        log.info("Consumed movie event: {}", event);
    }

    @KafkaListener(topics = "user-events", groupId = "events-group")
    public void consumeUserEvent(Object event) {
        log.info("Consumed user event: {}", event);
    }

    @KafkaListener(topics = "payment-events", groupId = "events-group")
    public void consumePaymentEvent(Object event) {
        log.info("Consumed payment event: {}", event);
    }
}
