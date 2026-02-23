package com.paypalclone.payout_service.service;

import com.paypalclone.payout_service.client.BalanceReadClient;
import com.paypalclone.payout_service.dtos.BalanceResponse;
import com.paypalclone.payout_service.entity.Payout;
import com.paypalclone.payout_service.entity.PayoutCandidate;
import com.paypalclone.payout_service.repository.PayoutCandidateRepository;
import com.paypalclone.payout_service.repository.PayoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayoutEvaluator {

    private static final BigDecimal MIN_PAYOUT =
            new BigDecimal("100.00");

    private final PayoutCandidateRepository candidateRepository;
    private final PayoutRepository payoutRepository;
    private final PayoutEligibilityService eligibilityService;
    private final BalanceReadClient balanceReadClient;

    @Scheduled(fixedDelay = 30_000)
    @Transactional
    public void evaluate() {

        List<PayoutCandidate> candidates =
                candidateRepository.findByStatus(
                        PayoutCandidate.Status.PENDING
                );

        if (candidates.isEmpty()) {
            return;
        }

        log.info("Evaluating {} payout candidates", candidates.size());

        for (PayoutCandidate candidate : candidates) {
            try {
                processCandidate(candidate);
            } catch (Exception ex) {
                // DO NOT break the loop
                log.error(
                        "Failed to process payout candidate intentId={}",
                        candidate.getPaymentIntentId(),
                        ex
                );
            }
        }
    }

    private void processCandidate(PayoutCandidate candidate) {

        Long merchantLedgerAccountId =
                candidate.getMerchantLedgerAccountId();

        if (!eligibilityService.isEligible(merchantLedgerAccountId)) {
            log.info(
                    "Merchant not eligible yet ledgerAccountId={}",
                    merchantLedgerAccountId
            );
            return;
        }

        BalanceResponse balance =
                balanceReadClient.getBalance(
                        merchantLedgerAccountId,
                        candidate.getCurrency()
                );


        BigDecimal available =
                balance.available() != null
                        ? balance.available()
                        : BigDecimal.ZERO;

        if (available.compareTo(MIN_PAYOUT) < 0) {
            log.info(
                    "Balance below threshold ledgerAccountId={}, balance={}",
                    merchantLedgerAccountId,
                    available
            );
            return;
        }


        String idempotencyKey =
                "PAYOUT_" + merchantLedgerAccountId + "_"
                        + balance.updatedAt();


        if (payoutRepository.findByIdempotencyKey(idempotencyKey).isPresent()) {
            log.info(
                    "Payout already exists key={}",
                    idempotencyKey
            );
            candidate.markProcessed();
            return;
        }

        Payout payout =
                Payout.create(
                        merchantLedgerAccountId,
                        merchantLedgerAccountId,
                        balance.available(),
                        candidate.getCurrency(),
                        idempotencyKey
                );

        payoutRepository.save(payout);

        candidate.markProcessed();

        log.info(
                "PAYOUT CREATED ledgerAccountId={}, amount={}",
                merchantLedgerAccountId,
                payout.getAmount()
        );
    }
}