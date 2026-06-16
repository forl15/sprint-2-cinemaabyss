package com.cinemaabyss.events.model;

public record MovieEvent(
    Integer movie_id,
    String title,
    String action,
    Integer user_id) {
}
