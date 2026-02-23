package com.paypalclone.payout_service.service;

import com.paypalclone.payout_service.entity.Payout;
import com.paypalclone.payout_service.kafka.PayoutEventPublisher;
import com.paypalclone.payout_service.repository.PayoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayoutBankConfirmationService {

    private final PayoutRepository payoutRepository;
    private final PayoutEventPublisher eventPublisher;


    @Transactional
    public void confirmSuccess(Long payoutId) {

        Payout payout =
                payoutRepository.findById(payoutId)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Payout not found id=" + payoutId
                                )
                        );

        payout.complete();                    //  internal state
        eventPublisher.publishCompleted(payout); //  external fact

        log.info(
                "Payout COMPLETED payoutId={}, ledgerAccountId={}, amount={}",
                payout.getId(),
                payout.getLedgerAccountId(),
                payout.getAmount()
        );
    }
}