package com.paypalclone.payout_service.kafka;

import com.paypalclone.PaymentIntent.PaymentIntentCapturedEvent;
import com.paypalclone.payout_service.entity.CapturedPayment;
import com.paypalclone.payout_service.repository.CapturedPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentIntentCapturedConsumer {

    private final CapturedPaymentRepository repository;

    @KafkaListener(
            topics = "payment.intent.captured",
            containerFactory = "paymentIntentCapturedKafkaListenerContainerFactory"
    )
    public void onCaptured(PaymentIntentCapturedEvent event) {

        repository.findByPaymentIntentId(event.getPaymentIntentId())
                .ifPresentOrElse(
                        cp -> {}, // idempotent
                        () -> {
                            repository.save(
                                    CapturedPayment.fromEvent(event)
                            );
                            log.info(
                                    "Captured payment stored intentId={}",
                                    event.getPaymentIntentId()
                            );
                        }
                );
    }
}
