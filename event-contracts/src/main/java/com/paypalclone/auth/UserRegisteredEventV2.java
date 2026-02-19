package com.paypalclone.auth;

import com.paypalclone.base.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserRegisteredEventV2 extends BaseEvent {

    private  Long userId;
    private  String email;
    private  String username;
}
