package com.paypalclone.merchant;

import com.paypalclone.base.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantSuspendedEvent extends BaseEvent {

    private  Long merchantId;
    private  Long userId;
}
