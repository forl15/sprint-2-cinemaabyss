package com.cinemaabyss.events.model;

import lombok.Builder;

@Builder
public record EventResponse(
    String status,
    Integer partition,
    Long offset,
    Event event
) {
}
