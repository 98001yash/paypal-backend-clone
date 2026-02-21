package com.paypalclone.payment_intent_service.repository;

import com.paypalclone.payment_intent_service.entity.PaymentIntent;
import com.paypalclone.payment_intent_service.enums.PaymentIntentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentIntentRepository extends JpaRepository<PaymentIntent, Long> {


    Optional<PaymentIntent> findByIdempotencyKey(String idempotencyKey);

    Optional<PaymentIntent> findByOrderId(Long orderId);


    List<PaymentIntent> findByStatus(PaymentIntentStatus status);

    List<PaymentIntent> findByBuyerId(Long buyerId);

}
