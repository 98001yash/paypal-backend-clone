package com.paypalclone.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Getter
@SuperBuilder
public abstract class BaseEvent {

    @JsonProperty("event_id")
    private final String eventId;

    @JsonProperty("event_type")
    private final String eventType;

    @JsonProperty("event_version")
    private final int eventVersion;

    @JsonProperty("occurred_at")
    private final Instant occurredAt;

    // Lombok will use this
    protected BaseEvent(
            String eventId,
            String eventType,
            int eventVersion,
            Instant occurredAt
    ) {
        this.eventId = eventId != null ? eventId : UUID.randomUUID().toString();
        this.eventType = eventType;
        this.eventVersion = eventVersion;
        this.occurredAt = occurredAt != null ? occurredAt : Instant.now();
    }
}
