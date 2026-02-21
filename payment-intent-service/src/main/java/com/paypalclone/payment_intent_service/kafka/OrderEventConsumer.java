package com.paypalclone.payment_intent_service.kafka;


import com.paypalclone.orders.OrderConfirmedEvent;
import com.paypalclone.orders.OrderCreatedEvent;
import com.paypalclone.payment_intent_service.enums.PaymentMethodType;
import com.paypalclone.payment_intent_service.service.PaymentIntentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final PaymentIntentService paymentIntentService;


    @KafkaListener(
            topics = KafkaTopics.ORDER_CREATED,
            containerFactory = "orderCreatedKafkaListenerContainerFactory"
    )
    public void onOrderCreated(OrderCreatedEvent event) {

        log.info(
                "Received OrderCreatedEvent orderId={}, buyer={}, total={}",
                event.getOrderId(),
                event.getBuyerId(),
                event.getTotalAmount()
        );
        paymentIntentService.createIntentIfNotExists(
                event.getOrderId(),
                event.getBuyerId(),
                event.getMerchantId(),
                event.getTotalAmount(),
                event.getCurrency(),
                PaymentMethodType.WALLET,
                "order-" + event.getOrderId()
        );
    }


    @KafkaListener(
            topics = KafkaTopics.ORDER_CONFIRMED,
            containerFactory = "orderConfirmedKafkaListenerContainerFactory"
    )
    public void onOrderConfirmed(OrderConfirmedEvent event) {

        log.info(
                "Received OrderConfirmedEvent orderId={}",
                event.getOrderId()
        );
        paymentIntentService.authorizeByOrderId(event.getOrderId());
    }
}
