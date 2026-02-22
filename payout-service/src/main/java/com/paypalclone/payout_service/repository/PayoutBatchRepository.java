package com.paypalclone.payout_service.repository;

import com.paypalclone.payout_service.entity.PayoutBatch;
import com.paypalclone.payout_service.enums.PayoutBatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayoutBatchRepository extends JpaRepository<PayoutBatch, Long> {

    // Idempotency

    Optional<PayoutBatch> findByBatchKey(String batchKey);

    //  Lifecycle queries
    List<PayoutBatch> findByStatus(PayoutBatchStatus status);
}
