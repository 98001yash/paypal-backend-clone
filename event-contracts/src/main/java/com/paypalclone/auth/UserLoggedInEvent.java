package com.paypalclone.auth;

import com.paypalclone.base.BaseEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UserLoggedInEvent extends BaseEvent {

    private final Long userId;
    private final String email;
}
