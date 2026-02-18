package com.paypalclone.user;

import com.paypalclone.base.BaseEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UserCreatedEvent extends BaseEvent {

    private final String externalAuthId;
    private final String email;
}
