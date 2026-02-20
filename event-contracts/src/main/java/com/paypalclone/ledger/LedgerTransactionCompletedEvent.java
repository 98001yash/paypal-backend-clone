package com.paypalclone.ledger;

import com.paypalclone.base.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LedgerTransactionCompletedEvent extends BaseEvent {

    private Long ledgerTransactionId;
    private String referenceType;
    private String referenceId;
}