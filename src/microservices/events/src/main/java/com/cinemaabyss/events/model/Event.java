package com.cinemaabyss.events.model;

import lombok.Builder;

@Builder
public record Event(
    String id,
    String type,
    String timestamp,
    Object payload
) {
}
