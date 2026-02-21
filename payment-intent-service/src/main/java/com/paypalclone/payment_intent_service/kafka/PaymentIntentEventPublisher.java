package com.paypalclone.payment_intent_service.kafka;

import com.paypalclone.PaymentIntent.PaymentIntentAuthorizedEvent;
import com.paypalclone.PaymentIntent.PaymentIntentCapturedEvent;
import com.paypalclone.PaymentIntent.PaymentIntentCreatedEvent;
import com.paypalclone.PaymentIntent.PaymentIntentFailedEvent;
import com.paypalclone.payment_intent_service.entity.PaymentIntent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentIntentEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishCreated(PaymentIntent intent) {

        PaymentIntentCreatedEvent event =
                PaymentIntentCreatedEvent.builder()
                        .paymentIntentId(intent.getId())
                        .orderId(intent.getOrderId())
                        .buyerId(intent.getBuyerId())
                        .merchantId(intent.getMerchantId())
                        .amount(intent.getAmount())
                        .currency(intent.getCurrency())
                        .build();

        log.info(
                "Publishing PaymentIntentCreatedEvent intentId={}, orderId={}",
                intent.getId(), intent.getOrderId()
        );

        kafkaTemplate.send(
                KafkaTopics.PAYMENT_INTENT_CREATED,
                intent.getOrderId().toString(),
                event
        );
    }

    public void publishAuthorized(PaymentIntent intent) {

        PaymentIntentAuthorizedEvent event =
                PaymentIntentAuthorizedEvent.builder()
                        .paymentIntentId(intent.getId())
                        .orderId(intent.getOrderId())
                        .build();

        log.info(
                "Publishing PaymentIntentAuthorizedEvent intentId={}",
                intent.getId()
        );

        kafkaTemplate.send(
                KafkaTopics.PAYMENT_INTENT_AUTHORIZED,
                intent.getOrderId().toString(),
                event
        );
    }

    public void publishCaptured(PaymentIntent intent) {

        PaymentIntentCapturedEvent event =
                PaymentIntentCapturedEvent.builder()
                        .paymentIntentId(intent.getId())
                        .orderId(intent.getOrderId())
                        .build();

        log.info(
                "Publishing PaymentIntentCapturedEvent intentId={}",
                intent.getId()
        );

        kafkaTemplate.send(
                KafkaTopics.PAYMENT_INTENT_CAPTURED,
                intent.getOrderId().toString(),
                event
        );
    }

    public void publishFailed(PaymentIntent intent, String reason) {

        PaymentIntentFailedEvent event =
                PaymentIntentFailedEvent.builder()
                        .paymentIntentId(intent.getId())
                        .orderId(intent.getOrderId())
                        .reason(reason)
                        .build();

        log.warn(
                "Publishing PaymentIntentFailedEvent intentId={}, reason={}",
                intent.getId(), reason
        );

        kafkaTemplate.send(
                KafkaTopics.PAYMENT_INTENT_FAILED,
                intent.getOrderId().toString(),
                event
        );
    }
}
