package com.paypalclone.user;

import com.paypalclone.base.BaseEvent;
import com.paypalclone.user.enums.KycLevel;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


@Getter
@SuperBuilder
public class UserKycUpdatedEvent extends BaseEvent {

    private final String externalAuthId;
    private final KycLevel oldLevel;
    private final KycLevel newLevel;
}
