package com.paypalclone.payout_service.dtos;

public record BankPayoutResultEvent(
        String batchKey,
        boolean success
) {}
