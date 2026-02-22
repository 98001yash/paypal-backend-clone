package com.paypalclone.payout_service.kafka;

import com.paypalclone.ledger.LedgerTransactionCompletedEvent;
import com.paypalclone.payout_service.entity.CapturedPayment;
import com.paypalclone.payout_service.entity.PayoutCandidate;
import com.paypalclone.payout_service.repository.CapturedPaymentRepository;
import com.paypalclone.payout_service.repository.PayoutCandidateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LedgerTransactionCompletedConsumer {

    private final CapturedPaymentRepository capturedPaymentRepository;
    private final PayoutCandidateRepository payoutCandidateRepository;

    @KafkaListener(
            topics = "ledger.transaction.completed",
            containerFactory = "ledgerTransactionCompletedKafkaListenerContainerFactory"
    )
    public void onTransactionCompleted(
            LedgerTransactionCompletedEvent event
    ) {

        // Only care about PAYMENT settlements
        if (!"PAYMENT".equals(event.getReferenceType())) {
            return;
        }

        Long paymentIntentId =
                Long.valueOf(event.getReferenceId());

        // Idempotency: payout candidate already created
        payoutCandidateRepository
                .findByPaymentIntentId(paymentIntentId)
                .ifPresent(pc -> {
                    log.info(
                            "PayoutCandidate already exists for intentId={}",
                            paymentIntentId
                    );
                    return;
                });

        CapturedPayment captured =
                capturedPaymentRepository
                        .findByPaymentIntentId(paymentIntentId)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "CapturedPayment not found for intent "
                                                + paymentIntentId
                                )
                        );

        PayoutCandidate candidate =
                PayoutCandidate.create(
                        paymentIntentId,
                        captured.getMerchantLedgerAccountId(),
                        captured.getCurrency()
                );

        payoutCandidateRepository.save(candidate);

        log.info(
                "PayoutCandidate CREATED intentId={}, merchantLedgerAccountId={}",
                paymentIntentId,
                captured.getMerchantLedgerAccountId()
        );
    }
}