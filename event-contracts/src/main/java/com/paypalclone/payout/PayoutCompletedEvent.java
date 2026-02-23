package com.paypalclone.payout;

import com.paypalclone.base.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PayoutCompletedEvent extends BaseEvent {

    private Long payoutId;
    private Long ledgerAccountId;
    private BigDecimal amount;
    private String currency;
}
