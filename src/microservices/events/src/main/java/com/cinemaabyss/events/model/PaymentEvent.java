package com.cinemaabyss.events.model;

public record PaymentEvent(
    Integer payment_id,
    Integer user_id,
    Double amount,
    String status,
    String timestamp,
    String method_type
) {
}
