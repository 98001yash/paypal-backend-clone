package com.paypalclone.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.paypalclone.base.BaseEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UserRegisteredEvent extends BaseEvent {

    private final Long userId;
    private final String email;

    @JsonCreator
    public UserRegisteredEvent(
            @JsonProperty("event_id") String eventId,
            @JsonProperty("event_type") String eventType,
            @JsonProperty("event_version") int eventVersion,
            @JsonProperty("occurred_at") java.time.Instant occurredAt,
            @JsonProperty("userId") Long userId,
            @JsonProperty("email") String email
    ) {
        super(eventId, eventType, eventVersion, occurredAt);
        this.userId = userId;
        this.email = email;
    }
}
