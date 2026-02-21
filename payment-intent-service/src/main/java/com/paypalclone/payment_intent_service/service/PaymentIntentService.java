package com.paypalclone.payment_intent_service.service;


import com.paypalclone.payment_intent_service.entity.PaymentIntent;
import com.paypalclone.payment_intent_service.enums.PaymentMethodType;
import com.paypalclone.payment_intent_service.repository.PaymentIntentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentIntentService {

    private final PaymentIntentRepository repository;


    @Transactional
    public PaymentIntent createIntentIfNotExists(
            Long orderId,
            Long buyerId,
            Long merchantId,
            BigDecimal amount,
            String currency,
            PaymentMethodType method,
            String idempotencyKey
    ){
        Optional<PaymentIntent> existing = repository.findByIdempotencyKey(idempotencyKey);

        if(existing.isPresent()){
            log.info("Payment intent already exists for key={}",idempotencyKey);
            return existing.get();
        }
        PaymentIntent intent = PaymentIntent.create(
                orderId,
                buyerId,
                merchantId,
                amount,
                currency,
                method,
                idempotencyKey
        );
        PaymentIntent saved = repository.save(intent);
        log.info(
                "PaymentIntent created id={} orderId={} amount={}",
                saved.getId(), orderId, amount
        );
        return saved;
    }

    @Transactional
    public void authorizeByOrderId(Long orderId) {

        PaymentIntent intent = repository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "PaymentIntent not found for orderId=" + orderId
                        )
                );

        intent.authorize();

        log.info(
                "PaymentIntent {} authorized for order {}",
                intent.getId(),
                orderId
        );
    }


    @Transactional
    public void capture(Long intentId) {

        PaymentIntent intent = get(intentId);
        intent.capture();
        log.info("PaymentIntent {} captured", intentId);
    }

    @Transactional
    public void fail(Long intentId) {

        PaymentIntent intent = get(intentId);
        intent.fail();
        log.warn("PaymentIntent {} failed", intentId);
    }

    @Transactional
    public void cancel(Long intentId) {

        PaymentIntent intent = get(intentId);
        intent.cancel();
        log.info("PaymentIntent {} cancelled", intentId);
    }


    @Transactional(readOnly = true)
    public PaymentIntent get(Long intentId) {
        return repository.findById(intentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "PaymentIntent not found: " + intentId
                        )
                );
    }
}
