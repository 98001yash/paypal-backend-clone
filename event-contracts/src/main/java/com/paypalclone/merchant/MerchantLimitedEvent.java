package com.paypalclone.merchant;


import com.paypalclone.base.BaseEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class MerchantLimitedEvent extends BaseEvent {

    private final Long merchantId;
    private final Long userId;

}