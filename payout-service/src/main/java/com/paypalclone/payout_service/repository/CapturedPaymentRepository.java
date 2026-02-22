package com.paypalclone.payout_service.repository;

import com.paypalclone.payout_service.entity.CapturedPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CapturedPaymentRepository
        extends JpaRepository<CapturedPayment, Long> {

    Optional<CapturedPayment> findByPaymentIntentId(Long paymentIntentId);
}