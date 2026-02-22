package com.paypalclone.payout_service.dtos;



import com.paypalclone.payout_service.entity.BalanceProjection;

import java.math.BigDecimal;
import java.time.Instant;

public record BalanceResponse(
        Long ledgerAccountId,
        String currency,
        BigDecimal available,
        BigDecimal pending,
        Instant updatedAt
) {
    public static BalanceResponse from(BalanceProjection bp) {
        return new BalanceResponse(
                bp.getLedgerAccountId(),
                bp.getCurrency(),
                bp.getAvailableBalance(),
                bp.getPendingBalance(),
                bp.getUpdatedAt()
        );
    }
}