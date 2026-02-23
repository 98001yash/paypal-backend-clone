package com.paypalclone.payout_service.service;

import com.paypalclone.payout_service.entity.Payout;
import com.paypalclone.payout_service.entity.PayoutBatch;
import com.paypalclone.payout_service.enums.PayoutStatus;
import com.paypalclone.payout_service.repository.PayoutBatchRepository;
import com.paypalclone.payout_service.repository.PayoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayoutBatchingService {

    private final PayoutRepository payoutRepository;
    private final PayoutBatchRepository payoutBatchRepository;

    /**
     * Runs every 1 minute
     */
    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void createBatch() {

        // 1️⃣ Fetch payouts ready for batching
        List<Payout> payouts =
                payoutRepository.findTop100ByStatus(
                        PayoutStatus.CREATED
                );

        if (payouts.isEmpty()) {
            return;
        }

        // 2️⃣ For now: assume same currency
        String currency = payouts.get(0).getCurrency();

        BigDecimal totalAmount =
                payouts.stream()
                        .map(Payout::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3️⃣ Create batch key (idempotent & human readable)
        String batchKey =
                "BATCH_" + currency + "_" + Instant.now().toEpochMilli();

        PayoutBatch batch =
                PayoutBatch.create(
                        batchKey,
                        totalAmount,
                        currency
                );

        payoutBatchRepository.save(batch);

        // 4️⃣ Assign payouts to batch
        for (Payout payout : payouts) {
            payout.queue(batch.getId().toString());
        }

        log.info(
                "PayoutBatch created key={}, payouts={}, total={}",
                batchKey,
                payouts.size(),
                totalAmount
        );
    }
}