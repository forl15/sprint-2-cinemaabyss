package com.cinemaabyss.events.model;

public record UserEvent(
    Integer user_id,
    String username,
    String action,
    String timestamp
) {
}
