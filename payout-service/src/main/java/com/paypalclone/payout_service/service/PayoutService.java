package com.paypalclone.payout_service.service;

import com.paypalclone.payout_service.client.BalanceReadClient;
import com.paypalclone.payout_service.dtos.BalanceResponse;
import com.paypalclone.payout_service.entity.CapturedPayment;
import com.paypalclone.payout_service.entity.Payout;
import com.paypalclone.payout_service.repository.CapturedPaymentRepository;
import com.paypalclone.payout_service.repository.PayoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PayoutService {

    private static final BigDecimal MIN_PAYOUT =
            new BigDecimal("100.00");

    private final CapturedPaymentRepository capturedPaymentRepository;
    private final PayoutRepository payoutRepository;
    private final PayoutEligibilityService eligibilityService;
    private final BalanceReadClient balanceReadClient;

    /**
     * Called ONLY from LedgerTransactionConsumer
     */
    public void onPaymentSettled(String paymentIntentIdStr) {

        Long paymentIntentId = Long.valueOf(paymentIntentIdStr);

        // 1️⃣ Resolve captured payment (local read-model)
        CapturedPayment captured =
                capturedPaymentRepository
                        .findByPaymentIntentId(paymentIntentId)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Captured payment not found for intent "
                                                + paymentIntentId
                                )
                        );

        Long merchantLedgerAccountId =
                captured.getMerchantLedgerAccountId();

        // 2️⃣ Eligibility check (gate)
        if (!eligibilityService.isEligible(merchantLedgerAccountId)) {
            log.info(
                    "Merchant not eligible for payout ledgerAccountId={}",
                    merchantLedgerAccountId
            );
            return;
        }

        // 3️⃣ Read balance from Balance Projection service (HTTP)
        BalanceResponse balance =
                balanceReadClient.getBalance(
                        merchantLedgerAccountId,
                        captured.getCurrency()
                );

        if (balance.available()
                .compareTo(MIN_PAYOUT) < 0) {

            log.info(
                    "Balance below payout threshold ledgerAccountId={}, balance={}",
                    merchantLedgerAccountId,
                    balance.available()
            );
            return;
        }

        // 4️⃣ Stable idempotency key (DETERMINISTIC)
        String idempotencyKey =
                "PAYOUT_" + merchantLedgerAccountId
                        + "_" + captured.getCurrency();

        if (payoutRepository.findByIdempotencyKey(idempotencyKey).isPresent()) {
            log.info(
                    "Payout already exists for key={}",
                    idempotencyKey
            );
            return;
        }

        // 5️⃣ Create payout (NO MONEY MOVEMENT)
        Payout payout = Payout.create(
                merchantLedgerAccountId,      // merchantId (current mapping)
                merchantLedgerAccountId,      // ledgerAccountId
                balance.available(),   // snapshot amount
                captured.getCurrency(),
                idempotencyKey
        );

        payoutRepository.save(payout);

        log.info(
                "Payout CREATED ledgerAccountId={}, amount={}",
                merchantLedgerAccountId,
                payout.getAmount()
        );
    }
}