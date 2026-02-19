package com.paypalclone.user;

import com.paypalclone.base.BaseEvent;
import com.paypalclone.user.enums.KycLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserKycUpdatedEvent extends BaseEvent {

    private  Long userId;
    private  String externalAuthId;
    private  KycLevel oldLevel;
    private  KycLevel newLevel;
}
