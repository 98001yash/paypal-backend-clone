package com.paypalclone.account;

import com.paypalclone.base.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseAccountEvent extends BaseEvent {

    private Long accountId;
    private Long ownerId;
    private String accountType;
}
