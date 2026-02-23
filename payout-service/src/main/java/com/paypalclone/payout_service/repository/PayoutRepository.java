package com.paypalclone.payout_service.repository;

import com.paypalclone.payout_service.entity.Payout;
import com.paypalclone.payout_service.enums.PayoutStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayoutRepository extends JpaRepository<Payout,Long> {

    Optional<Payout> findByIdempotencyKey(String idempotencyKey);

    //Lifecycle queries

    List<Payout> findByStatus(PayoutStatus status);

    List<Payout> findByBatchId(String batchId);

    // Merchant visibility
    List<Payout> findByMerchantId(Long merchantId);

    List<Payout> findByMerchantIdAndStatus(
            Long merchantId,
            PayoutStatus status
    );

    List<Payout> findTop100ByStatus(PayoutStatus payoutStatus);

}
