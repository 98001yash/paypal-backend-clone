package com.paypalclone.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Getter
@SuperBuilder
@NoArgsConstructor(force = true)
public abstract class BaseEvent {

    private final String eventId;
    private final String eventType;
    private final int eventVersion;
    private final Instant occurredAt;

    @JsonCreator
    protected BaseEvent(
            @JsonProperty("event_id") String eventId,
            @JsonProperty("event_type") String eventType,
            @JsonProperty("event_version") int eventVersion,
            @JsonProperty("occurred_at") Instant occurredAt
    ) {
        this.eventId = eventId != null ? eventId : UUID.randomUUID().toString();
        this.eventType = eventType;
        this.eventVersion = eventVersion;
        this.occurredAt = occurredAt != null ? occurredAt : Instant.now();
    }
}
