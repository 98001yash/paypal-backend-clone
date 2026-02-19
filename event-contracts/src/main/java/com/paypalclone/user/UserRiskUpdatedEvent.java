package com.paypalclone.user;


import com.paypalclone.base.BaseEvent;
import com.paypalclone.user.enums.RiskState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserRiskUpdatedEvent extends BaseEvent {

    private  Long userId;
    private  String externalAuthId;
    private  RiskState oldState;
    private  RiskState newState;
}
