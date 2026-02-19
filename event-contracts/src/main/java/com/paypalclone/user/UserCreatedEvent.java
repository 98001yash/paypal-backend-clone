package com.paypalclone.user;

import com.paypalclone.base.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreatedEvent extends BaseEvent {

    private Long userId;
    private String externalAuthId;
    private  String email;
}
