package com.paypalclone.user;


import com.paypalclone.base.BaseEvent;
import com.paypalclone.user.enums.RiskState;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UserRiskUpdatedEvent extends BaseEvent {

    private final Long userId;
    private final String externalAuthId;
    private final RiskState oldState;
    private final RiskState newState;
}
