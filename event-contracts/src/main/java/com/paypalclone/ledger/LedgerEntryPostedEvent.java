package com.paypalclone.ledger;

import com.paypalclone.base.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LedgerEntryPostedEvent extends BaseEvent {

    private Long ledgerTransactionId;
    private Long ledgerAccountId;
    private String entryType;
    private BigDecimal amount;
    private String currency;
}