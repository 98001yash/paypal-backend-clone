package com.paypalclone.payout_service.repository;

import com.paypalclone.payout_service.entity.PayoutCandidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayoutCandidateRepository
        extends JpaRepository<PayoutCandidate, Long> {

    Optional<PayoutCandidate>
    findByPaymentIntentId(Long paymentIntentId);

    List<PayoutCandidate>
    findByStatus(PayoutCandidate.Status status);

    boolean existsByPaymentIntentId(Long paymentIntentId);

}