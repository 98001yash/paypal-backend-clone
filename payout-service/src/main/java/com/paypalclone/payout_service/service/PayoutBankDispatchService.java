package com.paypalclone.payout_service.service;

import com.paypalclone.payout_service.client.BankTransferClient;
import com.paypalclone.payout_service.entity.Payout;
import com.paypalclone.payout_service.entity.PayoutBatch;
import com.paypalclone.payout_service.enums.PayoutBatchStatus;
import com.paypalclone.payout_service.repository.PayoutBatchRepository;
import com.paypalclone.payout_service.repository.PayoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayoutBankDispatchService {

    private final PayoutBatchRepository batchRepository;
    private final PayoutRepository payoutRepository;
    private final BankTransferClient bankClient;

    /**
     * Runs every minute
     */
    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void dispatch() {

        List<PayoutBatch> batches =
                batchRepository.findByStatus(
                        PayoutBatchStatus.CREATED
                );

        if (batches.isEmpty()) {
            return;
        }

        for (PayoutBatch batch : batches) {

            List<Payout> payouts =
                    payoutRepository.findByBatchId(
                            batch.getId().toString()
                    );

            if (payouts.isEmpty()) {
                log.warn(
                        "Batch {} has no payouts, skipping",
                        batch.getBatchKey()
                );
                continue;
            }

            // 1️⃣ Send to bank
            bankClient.sendBatch(
                    batch.getBatchKey(),
                    payouts
            );

            // 2️⃣ Update payout state
            payouts.forEach(Payout::markSentToBank);

            // 3️⃣ Update batch state
            batch.markSentToBank();

            log.info(
                    "Batch {} sent to bank successfully",
                    batch.getBatchKey()
            );
        }
    }
}